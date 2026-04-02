package com.saas.coaching.service;

import com.saas.coaching.dto.AuthRequestDTO;
import com.saas.coaching.dto.AuthResponseDTO;
import com.saas.coaching.dto.CurrentInstituteDTO;
import com.saas.coaching.dto.RegisterRequestDTO;
import com.saas.coaching.entity.Institute;
import com.saas.coaching.exception.EmailAlreadyExistsException;
import com.saas.coaching.repository.InstituteRepository;
import com.saas.coaching.security.CustomUserPrincipal;
import com.saas.coaching.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final InstituteRepository instituteRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public void register(RegisterRequestDTO request) {
        if (instituteRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("An institute account with this email already exists");
        }

        Institute institute = Institute.builder()
                .instituteName(request.getInstituteName())
                .ownerName(request.getOwnerName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .city(request.getCity())
                .build();

        instituteRepository.save(institute);
    }

    public AuthResponseDTO login(AuthRequestDTO request) {
        CustomUserPrincipal principal = (CustomUserPrincipal) authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()))
                .getPrincipal();

        String token = jwtService.generateToken(principal);

        return AuthResponseDTO.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(jwtService.getExpirationInMillis())
                .email(principal.getUsername())
                .instituteName(principal.getInstituteName())
                .build();
    }

    public CurrentInstituteDTO getCurrentInstitute(CustomUserPrincipal principal) {
        return CurrentInstituteDTO.builder()
                .instituteId(principal.getInstituteId())
                .instituteName(principal.getInstituteName())
                .email(principal.getUsername())
                .build();
    }
}
