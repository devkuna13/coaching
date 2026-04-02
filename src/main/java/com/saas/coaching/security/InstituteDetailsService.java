package com.saas.coaching.security;

import com.saas.coaching.entity.Institute;
import com.saas.coaching.repository.InstituteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InstituteDetailsService implements UserDetailsService {

    private final InstituteRepository instituteRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Institute institute = instituteRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid email or password"));

        return new CustomUserPrincipal(institute);
    }
}
