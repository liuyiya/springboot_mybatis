package com.evolution.interceptor;

import cn.hutool.core.util.StrUtil;
import com.evolution.domain.auth.service.AuthService;
import com.evolution.types.enums.ErrorCodeEnum;
import com.evolution.types.exception.AppException;
import com.evolution.domain.auth.model.constant.AuthRedisConstant;
import com.evolution.types.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器
 */
@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired, @Lazy}))
public class LoginInterceptor implements HandlerInterceptor {

    private final AuthService authService;
    private final RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token == null) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            throw new AppException(ErrorCodeEnum.token_non_exist);
        }
        boolean success = authService.checkToken(token);
        if (!success) {
            throw new AppException(ErrorCodeEnum.token_expired);
        }
        String userId = authService.userId(token);
        //判断redis中是否存在
        token = (String) redisService.get(StrUtil.format(AuthRedisConstant.evolution_string_user_token, userId));
        if (token == null) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            throw new AppException(ErrorCodeEnum.token_expired);
        }
        this.setAppAttribute(request, userId);
        return true;
    }

    private void setAppAttribute(HttpServletRequest request, String userId) {

        request.setAttribute("userId", userId);

    }

}
