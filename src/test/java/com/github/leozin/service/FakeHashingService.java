package com.github.leozin.service;

public class FakeHashingService implements HashingService {

    @Override
    public String encryptString(String value) {
        return "hashed-" + value;
    }
}
