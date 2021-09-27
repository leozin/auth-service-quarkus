package com.github.leozin.integration;

import com.github.leozin.User;
import com.github.leozin.repository.JPAUserRepository;
import com.github.leozin.repository.UserEntity;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class JPAUserRepositoryIT {

    @Inject
    JPAUserRepository userRepository;

    @BeforeEach
    @Transactional
    public void cleanup() {
        UserEntity.deleteAll();
    }

    @Test
    public void canPersist() {
        User user = new User("test@test.com", "a", null, null);
        userRepository.save(user);

        assertEquals(1, userRepository.findAll().stream().count());
    }

    @Test
    public void canUpdateUserDetails() {
        User user = new User("test@test.com", "a", null, null);
        userRepository.save(user);

        user.setFirstName("newfirst");
        user.setLastName("newlast");

        userRepository.update(user);

        User userByEmail = userRepository.findUserByEmail("test@test.com");

        assertEquals("newfirst", userByEmail.getFirstName());
        assertEquals("newlast", userByEmail.getLastName());
    }
}
