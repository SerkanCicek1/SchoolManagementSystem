package com.schoolmanagement.security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

    @Value("${backendapi.app.jwtSecret}")
    private String jwtSecret;

    @Value("${backendapi.app.jwtExpressionMS}")
    private long jwtExpirationMs;


    // Not: Generate JWT *************************************************


    // Not: Validate JWT *************************************************


    // Not: getUsernameForJWT ********************************************
}