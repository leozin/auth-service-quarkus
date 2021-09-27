package com.github.leozin.service;

import io.smallrye.jwt.build.Jwt;

import javax.enterprise.context.ApplicationScoped;
import java.util.Arrays;
import java.util.HashSet;

@ApplicationScoped
public class JwtTokenService implements TokenService {

    public String generateToken(String email) {
        return Jwt.issuer("github.com/leozin").upn(email)
                .expiresIn(30L)
                .groups(new HashSet<>(Arrays.asList("user")))
                .sign();
    }

}
