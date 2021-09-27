package com.github.leozin.api;

import com.github.leozin.dto.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Response;

public interface AuthenticationServiceAPI {

    SignupResponse signup(@Valid @NotNull SignupRequest request);

    LoginResponse login(@Valid @NotNull LoginRequest request);

    RetrieveUsersResponse retrieveUsers();

    Response updateUserDetails(UpdateUserDetailsRequest request);

}
