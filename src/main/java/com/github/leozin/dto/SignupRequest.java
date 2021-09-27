package com.github.leozin.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public record SignupRequest(@NotNull @Length(max = 255) @Email String email,
                            @NotNull @Length(max = 255) String password,
                            @Length(max = 255) String firstName, @Length(max = 255) String lastName) {
}
