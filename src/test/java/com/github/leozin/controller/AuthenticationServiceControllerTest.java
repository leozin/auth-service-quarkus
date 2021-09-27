package com.github.leozin.controller;

import com.github.leozin.dto.RetrieveUsersResponse;
import com.github.leozin.dto.SignupRequest;
import com.github.leozin.repository.FakeUserRepository;
import com.github.leozin.service.FakeHashingService;
import com.github.leozin.service.FakeTokenService;
import com.github.leozin.service.HashingService;
import com.github.leozin.service.UserService;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
public class AuthenticationServiceControllerTest {

    AuthenticationServiceController authenticationServiceController;

    FakeUserRepository repository;

    @BeforeEach
    void setupMocks() {
        repository = new FakeUserRepository();
        HashingService hashingService = new FakeHashingService();

        authenticationServiceController = new AuthenticationServiceController();
        authenticationServiceController.userService = new UserService(repository, hashingService);
        authenticationServiceController.tokenService = new FakeTokenService();
    }

    @Test
    public void signupTest() {
        SignupRequest request = new SignupRequest("email", "password", null, null);
        MatcherAssert.assertThat(authenticationServiceController.signup(request).token(), equalTo("token"));
    }

    @Test
    public void hashedPasswordTest() {
        SignupRequest request = new SignupRequest("email", "password", null, null);
        authenticationServiceController.signup(request);

        repository.findUserByEmail("email").getPassword().equals("hashed-password");
    }

    @Test
    public void getAllUsersTest() {
        // given
        SignupRequest user1 = new SignupRequest("email1", "password", null, null);
        SignupRequest user2 = new SignupRequest("email2", "password", null, null);
        SignupRequest user3 = new SignupRequest("email3", "password", null, null);
        authenticationServiceController.signup(user1);
        authenticationServiceController.signup(user2);
        authenticationServiceController.signup(user3);

        // when
        RetrieveUsersResponse response = authenticationServiceController.retrieveUsers();

        // then
        assertThat(Arrays.stream(response.users()).map(o -> o.email()).collect(Collectors.toList()), containsInAnyOrder("email1", "email2", "email3"));
    }
}
