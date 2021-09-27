package com.github.leozin.service;

import io.quarkus.elytron.security.common.BcryptUtil;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class BcryptHashingService implements HashingService {

    private static final int ITERATIONS = 10;
    private final String salt;

    @Inject
    public BcryptHashingService(@ConfigProperty(name = "security.salt") String salt) {
        this.salt = salt;
    }

    public String encryptString(String value) {
        return BcryptUtil.bcryptHash(value, ITERATIONS, salt.getBytes());
    }

}
