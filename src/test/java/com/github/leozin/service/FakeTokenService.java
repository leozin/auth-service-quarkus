package com.github.leozin.service;

public class FakeTokenService implements TokenService {

    @Override
    public String generateToken(String email) {
        return "token";
    }

}
