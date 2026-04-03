package com.saas.coaching.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "RegisterInstituteRequest", description = "Institute registration request for the SaaS platform")
public class RegisterRequestDTO {

    @Schema(description = "Coaching institute name", example = "Gyanjyoti Coaching")
    @NotBlank
    @Size(max = 100)
    private String instituteName;

    @Schema(description = "Institute owner or admin full name", example = "Uttam Kumar")
    @NotBlank
    @Size(max = 100)
    private String ownerName;

    @Schema(description = "Institute account email", example = "owner@gyanjyoti.com")
    @Email
    @NotBlank
    private String email;

    @Schema(description = "Institute account password", example = "password123")
    @NotBlank
    @Size(min = 6)
    private String password;

    @Schema(description = "Institute contact phone number", example = "6998560067")
    @NotBlank
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phoneNumber;

    @Schema(description = "Institute city", example = "Melchhamunda")
    @NotBlank
    @Size(max = 100)
    private String city;
}
