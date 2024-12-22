package com.example.stad.WebApi.Security;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class JwtConfig {

    @Value("${jwt.secret}")
    private String secret;


    @Value("${jwt.expiration}")
    private long expiration; // In milliseconds

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration; // In milliseconds


    @PostConstruct
    public void printConfig() {
        System.out.println("JWT Secret: " + secret);
        System.out.println("JWT Expiration: " + expiration);
        System.out.println("JWT Refresh Expiration: " + refreshExpiration);
    }

}


