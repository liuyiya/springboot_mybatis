package com.evolution.domain.auth.service;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 鉴权服务
 */
@Slf4j
@Service
public class AuthServiceImpl extends AbstractAuthService {

    @Override
    public boolean checkToken(String token) {
        return isVerify(token);
    }

    @Override
    public String userId(String token) {
        Claims claims = decode(token);
        return claims.get("userId").toString();
    }

}
