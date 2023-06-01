package com.schoolmanagement.security.jwt;

import com.schoolmanagement.security.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${backendapi.app.jwtSecret}")
    private String jwtSecret;

    @Value("${backendapi.app.jwtExpressionMS}")
    private long jwtExpirationMs;


    // Not: Generate JWT *************************************************
    public String generateJwtToken(Authentication authentication) {

        //anlik olarak login islemi yapan kullanici bilgisi :
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        // username bilgisi ile JWT token uretiliyor
        return  generateTokenFromUsername(userPrincipal.getUsername());

    }

    public String generateTokenFromUsername(String username) {

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }




    // Not: Validate JWT *************************************************
    public boolean validateJwtToken(String authToken){

        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (ExpiredJwtException e) {
            logger.error("Jwt token is expired : {}" , e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT Token is unsupported : {}" , e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid Jwt Token: {}" , e.getMessage());
        } catch (SignatureException e) {
            logger.error("Invalid Jwt signature : {}" , e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Jwt claims string is empty: {}" , e.getMessage());
        }

        return false;
    }


    // Not: getUsernameForJWT ********************************************
    public String getUserNameFromJwtToken(String token) {

        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }
}