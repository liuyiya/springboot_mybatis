package com.evolution.trigger.http.auth;

import com.evolution.domain.auth.model.dto.UserLoginDto;
import com.evolution.domain.auth.model.dto.UserRegisterDto;
import com.evolution.domain.auth.service.AuthService;
import com.evolution.types.reponse.ResultBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 身份验证控制器
 */
@Slf4j
@RestController
@RequestMapping("/auth/")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 用户注册
     */
    @PostMapping(value = "register")
    public ResultBody<Void> register(@Valid @RequestBody UserRegisterDto user) {
        authService.register(user);
        return new ResultBody<>();
    }

    /**
     * 用户名密码登录
     */
    @PostMapping(value = "login")
    public ResultBody<Map<String, String>> login(@RequestBody UserLoginDto user) {
        String token = authService.doLogin(user);
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", "");
        return new ResultBody<>(tokenMap);
    }

    /**
     * 登出
     */
    @PostMapping(value = "logout")
    public ResultBody<Void> logout(@RequestAttribute Long userId) {
        authService.logout(userId);
        return new ResultBody<>();
    }


}
