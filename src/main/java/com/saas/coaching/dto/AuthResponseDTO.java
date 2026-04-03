package com.saas.coaching.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(name = "AuthResponse", description = "JWT authentication response for the institute account")
public class AuthResponseDTO {

    @Schema(description = "JWT access token", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1dHRhbUBnbWFpbC5jb20ifQ.signature")
    private String token;

    @Schema(description = "Authorization scheme for secured API calls", example = "Bearer")
    private String tokenType;

    @Schema(description = "Token expiry in milliseconds", example = "86400000")
    private Long expiresIn;

    @Schema(description = "Authenticated institute email", example = "owner@gyanjyoti.com")
    private String email;

    @Schema(description = "Authenticated institute name", example = "Gyanjyoti Coaching")
    private String instituteName;
}
