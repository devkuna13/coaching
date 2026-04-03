package com.saas.coaching.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(name = "AuthRequest", description = "Institute owner login request")
public class AuthRequestDTO {

    @Schema(description = "Registered institute email address", example = "owner@gyanjyoti.com")
    @Email
    @NotBlank
    private String email;

    @Schema(description = "Institute account password", example = "password123")
    @NotBlank
    private String password;
}
