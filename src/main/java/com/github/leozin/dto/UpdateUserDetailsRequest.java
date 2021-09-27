package com.github.leozin.dto;

import org.hibernate.validator.constraints.Length;

public record UpdateUserDetailsRequest(@Length(max = 255) String firstName, @Length(max = 255) String lastName) {
}
