package com.evolution.domain.auth.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.evolution.domain.auth.model.dto.UserLoginDto;
import com.evolution.domain.auth.model.dto.UserRegisterDto;
import com.evolution.domain.system.model.entity.SysUser;
import com.evolution.domain.system.service.UserService;
import com.evolution.types.exception.AppException;
import com.evolution.domain.auth.model.constant.AuthRedisConstant;
import com.evolution.types.redis.RedisService;
import com.evolution.types.utils.AESUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 抽象身份验证服务
 */
@Slf4j
public abstract class AbstractAuthService implements AuthService {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;

    @Value("${auth.AesKey}")
    private String AesKey;
    @Value("${auth.ttlMillis}")
    private Long ttlMillis;
    @Value("${auth.SecretKey}")
    private String SecretKey;
    private String base64EncodedSecretKey;
    private Algorithm algorithm;

    @PostConstruct
    public void init() {
        this.base64EncodedSecretKey = Base64.encodeBase64String(SecretKey.getBytes());
        this.algorithm = Algorithm.HMAC256(Base64.decodeBase64(Base64.encodeBase64String(SecretKey.getBytes())));
    }

    @Override
    public void register(UserRegisterDto user) {
        String username = user.getUsername();
        LambdaQueryWrapper<SysUser> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysUser::getUsername, username);
        SysUser sysUser = userService.getOne(wrapper);
        if (BeanUtil.isNotEmpty(sysUser)) {
            throw new AppException("用户名不能重复");
        }
        //获取随机盐
        String salt = RandomUtil.randomString(6);
        String password = AESUtil.encryptHex(user.getPassword() + salt, AesKey);
        sysUser = SysUser.builder()
                .salt(salt)
                .email(user.getEmail())
                .password(password)
                .remark(user.getRemark())
                .nickName(user.getNickName())
                .username(user.getUsername())
                .password(password).build();
        userService.addUser(sysUser);
    }

    @Override
    public String doLogin(UserLoginDto user) {
        String username = user.getUsername();
        String password = user.getPassword();
        SysUser sysUser = userService.getUserByUsername(username);
        if (BeanUtil.isEmpty(sysUser) || !AESUtil.decryptStr(sysUser.getPassword(), AesKey).equals(password + sysUser.getSalt())) {
            throw new AppException("用户名或密码错误");
        }
        Map<String, Object> chaim = new HashMap<>();
        chaim.put("userId", sysUser.getId());
        String token = encode(sysUser.getId().toString(), ttlMillis, chaim);
        //  缓存redis
        redisService.set(StrUtil.format(AuthRedisConstant.evolution_string_user_token, sysUser.getId(), 7 * 24 * 60 * 60 * 1000), token);
        return token;
    }

    @Override
    public void logout(Long userId) {
        redisService.del(StrUtil.format(AuthRedisConstant.evolution_string_user_token, userId));
    }

    /**
     * 这里就是产生jwt字符串的地方
     * jwt字符串包括三个部分
     * 1. header
     * -当前字符串的类型，一般都是“JWT”
     * -哪种算法加密，“HS256”或者其他的加密算法
     * 所以一般都是固定的，没有什么变化
     * 2. payload
     * 一般有四个最常见的标准字段（下面有）
     * iat：签发时间，也就是这个jwt什么时候生成的
     * jti：JWT的唯一标识
     * iss：签发人，一般都是username或者userId
     * exp：过期时间
     */
    protected String encode(String issuer, long ttlMillis, Map<String, Object> claims) {
        // iss签发人，ttlMillis生存时间，claims是指还想要在jwt中存储的一些非隐私信息
        if (claims == null) {
            claims = new HashMap<>();
        }
        long nowMillis = System.currentTimeMillis();

        JwtBuilder builder = Jwts.builder()
                // 荷载部分
                .setClaims(claims)
                // 这个是JWT的唯一标识，一般设置成唯一的，这个方法可以生成唯一标识
                .setId(UUID.randomUUID().toString())//2.
                // 签发时间
                .setIssuedAt(new Date(nowMillis))
                // 签发人，也就是JWT是给谁的（逻辑上一般都是username或者userId）
                .setSubject(issuer)
                .signWith(SignatureAlgorithm.HS256, base64EncodedSecretKey);//这个地方是生成jwt使用的算法和秘钥
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);// 4. 过期时间，这个也是使用毫秒生成的，使用当前时间+前面传入的持续时间生成
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    // 相当于encode的方向，传入jwtToken生成对应的username和password等字段。Claim就是一个map
    // 也就是拿到荷载部分所有的键值对
    protected Claims decode(String jwtToken) {
        // 得到 DefaultJwtParser
        return Jwts.parser()
                // 设置签名的秘钥
                .setSigningKey(base64EncodedSecretKey)
                // 设置需要解析的 jwt
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    // 判断jwtToken是否合法
    protected boolean isVerify(String jwtToken) {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(jwtToken);
            // 校验不通过会抛出异常
            // 判断合法的标准：1. 头部和荷载部分没有篡改过。2. 没有过期
            return true;
        } catch (Exception e) {
            log.error("jwt isVerify Err", e);
            return false;
        }

    }

}
