package com.github.leozin.dto;

public record RetrieveUsersResponse(User... users) {

    public record User(String email, String firstName, String lastName) {
    }
}
