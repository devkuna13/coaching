package com.saas.coaching.repository;

import com.saas.coaching.entity.Fee;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeeRepository extends JpaRepository<Fee, Long> {

    List<Fee> findAllByInstituteIdOrderByDueDateAscCreatedAtDesc(Long instituteId);

    List<Fee> findAllByInstituteIdAndStudentIdOrderByFeeYearDescFeeMonthDesc(Long instituteId, Long studentId);

    Optional<Fee> findByIdAndInstituteId(Long feeId, Long instituteId);

    boolean existsByInstituteIdAndStudentIdAndFeeMonthAndFeeYear(
            Long instituteId,
            Long studentId,
            Integer feeMonth,
            Integer feeYear);

    boolean existsByInstituteIdAndStudentIdAndFeeMonthAndFeeYearAndIdNot(
            Long instituteId,
            Long studentId,
            Integer feeMonth,
            Integer feeYear,
            Long feeId);
}
