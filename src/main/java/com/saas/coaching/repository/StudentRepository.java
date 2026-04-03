package com.saas.coaching.repository;

import com.saas.coaching.entity.Student;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findAllByInstituteIdOrderByCreatedAtDesc(Long instituteId);

    Optional<Student> findByIdAndInstituteId(Long studentId, Long instituteId);

    boolean existsByInstituteIdAndFullNameIgnoreCaseAndParentPhoneNumber(
            Long instituteId,
            String fullName,
            String parentPhoneNumber);

    boolean existsByInstituteIdAndFullNameIgnoreCaseAndParentPhoneNumberAndIdNot(
            Long instituteId,
            String fullName,
            String parentPhoneNumber,
            Long studentId);
}
