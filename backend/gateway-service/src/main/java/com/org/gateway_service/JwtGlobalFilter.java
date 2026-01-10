package com.org.gateway_service;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.auth0.jwt.JWT;

@Component
public class JwtGlobalFilter {

    @Value("${api.security.token.secret}")
    private String SECRET;
}
