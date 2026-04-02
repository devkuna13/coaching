package com.saas.coaching.repository;

import com.saas.coaching.entity.Institute;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstituteRepository extends JpaRepository<Institute, Long> {

    Optional<Institute> findByEmail(String email);
}
