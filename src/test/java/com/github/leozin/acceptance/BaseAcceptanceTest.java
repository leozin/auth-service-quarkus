package com.github.leozin.acceptance;

import com.github.leozin.repository.UserEntity;
import org.junit.jupiter.api.BeforeEach;

import javax.transaction.Transactional;

public class BaseAcceptanceTest {

    @BeforeEach
    @Transactional
    public void cleandb() {
        UserEntity.deleteAll();
    }
}
