package com.evolution.domain.auth.service;


import com.evolution.domain.auth.model.dto.UserLoginDto;
import com.evolution.domain.auth.model.dto.UserRegisterDto;

/**
 * 鉴权验证服务接口
 */
public interface AuthService {

    /**
     * 用户注册
     *
     * @param user 用户
     * @return {@link String}
     */
    void register(UserRegisterDto user);

    /**
     * 注销
     *
     * @param userId 用户 ID
     */
    void logout(Long userId);

    /**
     * 登录验证
     *
     * @param user 用户
     * @return {@link String}
     */
    String doLogin(UserLoginDto user);

    /**
     * 检查令牌
     *
     * @param token 令 牌
     * @return boolean
     */
    boolean checkToken(String token);

    /**
     * 用户 ID
     *
     * @param token 令 牌
     * @return {@link String}
     */
    String userId(String token);

}
