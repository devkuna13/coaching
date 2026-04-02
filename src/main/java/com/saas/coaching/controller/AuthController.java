package com.saas.coaching.controller;

import com.saas.coaching.dto.AuthRequestDTO;
import com.saas.coaching.dto.AuthResponseDTO;
import com.saas.coaching.dto.CurrentInstituteDTO;
import com.saas.coaching.dto.RegisterRequestDTO;
import com.saas.coaching.security.CustomUserPrincipal;
import com.saas.coaching.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(
            summary = "Register new institute",
            description = "Creates a coaching institute account for the institute owner with encrypted password")
    public String register(@Valid @RequestBody RegisterRequestDTO request) {
        authService.register(request);
        return "Institute registered successfully";
    }

    @PostMapping("/login")
    @Operation(
            summary = "Authenticate institute owner",
            description = "Validates institute owner credentials and returns a JWT access token for secured APIs")
    public AuthResponseDTO login(@Valid @RequestBody AuthRequestDTO request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "Get current institute profile",
            description = "Returns the authenticated institute account details from the JWT context")
    public CurrentInstituteDTO getCurrentInstitute(@AuthenticationPrincipal CustomUserPrincipal principal) {
        return authService.getCurrentInstitute(principal);
    }
}
