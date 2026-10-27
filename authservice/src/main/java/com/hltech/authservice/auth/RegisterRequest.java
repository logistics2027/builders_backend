package com.hltech.authservice.auth;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank
        @Size(max = 100)
        String fullName,

        @NotBlank
        @Email
        @Size(max = 254)
        String email,

        @NotBlank
        @Pattern(
            regexp = "^\\+[1-9][0-9]{7,14}$",
            message = "Use international format, e.g. +919876543210"
        )
        String phoneNumber,

        @NotBlank
        @Size(min = 8, max = 72)
        String password

) {
}
