package com.saas.coaching.security;

import com.saas.coaching.entity.Institute;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserPrincipal implements UserDetails {

    private final Long instituteId;
    private final String instituteName;
    private final String email;
    private final String password;
    private final List<GrantedAuthority> authorities;

    public CustomUserPrincipal(Institute institute) {
        this.instituteId = institute.getId();
        this.instituteName = institute.getInstituteName();
        this.email = institute.getEmail();
        this.password = institute.getPassword();
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_INSTITUTE_ADMIN"));
    }

    public Long getInstituteId() {
        return instituteId;
    }

    public String getInstituteName() {
        return instituteName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
