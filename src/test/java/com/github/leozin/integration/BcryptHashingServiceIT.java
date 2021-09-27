package com.github.leozin.integration;

import com.github.leozin.service.BcryptHashingService;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@QuarkusTest
public class BcryptHashingServiceIT {

    @Inject
    BcryptHashingService bcryptHashingService;

    @Test
    public void encryptDataTest() {
        String password = "password";
        String hashed = bcryptHashingService.encryptString(password);

        Assertions.assertNotEquals(password, hashed);
    }

    @Test
    public void encryptTwiceAndValidateSameHash() {
        String password = "password";
        String hashed1 = bcryptHashingService.encryptString(password);
        String hashed2 = bcryptHashingService.encryptString(password);

        Assertions.assertEquals(hashed1, hashed2);
    }

}
