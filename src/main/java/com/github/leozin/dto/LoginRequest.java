package com.github.leozin.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public record LoginRequest(@NotNull @Length(max = 255) @Email String email,
                           @NotNull @Length(max = 255) String password) {
}
