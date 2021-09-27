package com.github.leozin.controller;

import com.github.leozin.User;
import com.github.leozin.api.AuthenticationServiceAPI;
import com.github.leozin.dto.*;
import com.github.leozin.service.TokenService;
import com.github.leozin.service.UserService;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameters;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Valid
public class AuthenticationServiceController implements AuthenticationServiceAPI {

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationServiceController.class);
    @Inject
    JsonWebToken jwt;
    @Inject
    TokenService tokenService;
    @Inject
    UserService userService;

    @POST
    @Path("/signup")
    @PermitAll
    @Override
    @Operation(
            summary = "Signup using e-mail and password",
            description = "Creates an user using the provided details and return a valid JWT token to be used in protected endpoints")
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "422",
                            description = "E-mail Already in Use",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @APIResponse(
                            responseCode = "400",
                            description = "Invalid content",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @APIResponse(
                            responseCode = "200",
                            description = "User created successfully and a valid jwt token is generated",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SignupResponse.class)))})
    public SignupResponse signup(@Valid @NotNull SignupRequest request) {
        User user = new User(request.email(), request.password(), request.firstName(), request.lastName());
        userService.createNewUser(user);
        return new SignupResponse(tokenService.generateToken(request.email()));
    }

    @POST
    @Path("/login")
    @PermitAll
    @Override
    @Operation(
            summary = "Login",
            description = "Generated a new JWT token if provided credentials are correct")
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "401",
                            description = "Credentials do not match",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @APIResponse(
                            responseCode = "400",
                            description = "Invalid content",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @APIResponse(
                            responseCode = "200",
                            description = "Credentials match and a valid jwt token is generated",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LoginResponse.class)))})
    public LoginResponse login(@Valid @NotNull LoginRequest request) {
        User user = new User(request.email(), request.password(), null, null);
        userService.verifyCredentials(user);
        return new LoginResponse(tokenService.generateToken(request.email()));
    }

    @GET
    @Path("/users")
    @RolesAllowed({"user"})
    @Override
    @Operation(
            summary = "Retrieve Users",
            description = "Return all users currently saved in the database, without the password")
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "401",
                            description = "x-authentication-token header is null or invalid",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @APIResponse(
                            responseCode = "200",
                            description = "Returns the list of all users",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RetrieveUsersResponse.class)))})
    @Parameters(
            @Parameter(name = "x-authentication-token", in = ParameterIn.HEADER, description = "JWT token")
    )
    public RetrieveUsersResponse retrieveUsers() {
        List<User> allUsers = userService.retrieveAllUsers();
        return new RetrieveUsersResponse(
                allUsers
                        .stream()
                        .map(o -> new RetrieveUsersResponse.User(o.getEmail(), o.getFirstName(), o.getLastName()))
                        .toArray(RetrieveUsersResponse.User[]::new)
        );
    }

    @PUT
    @Path("/users")
    @RolesAllowed({"user"})
    @Override
    @Operation(
            summary = "Update details",
            description = "Updates the firstName and lastName of the current logged user")
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "401",
                            description = "x-authentication-token header is null or invalid",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @APIResponse(
                            responseCode = "204",
                            description = "Confirms that the data is updated")})
    @Parameters(
            @Parameter(name = "x-authentication-token", in = ParameterIn.HEADER, description = "JWT token")
    )
    public Response updateUserDetails(UpdateUserDetailsRequest request) {
        User user = new User(jwt.getName(), null, request.firstName(), request.lastName());

        userService.updateUser(user);

        return Response.status(Response.Status.NO_CONTENT).build();
    }

}