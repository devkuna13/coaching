package com.saas.coaching.service;

import com.saas.coaching.dto.CreateFeeRequestDTO;
import com.saas.coaching.dto.FeeResponseDTO;
import com.saas.coaching.dto.RecordFeePaymentRequestDTO;
import com.saas.coaching.dto.UpdateFeeRequestDTO;
import com.saas.coaching.entity.Fee;
import com.saas.coaching.entity.FeeStatus;
import com.saas.coaching.entity.Institute;
import com.saas.coaching.entity.Student;
import com.saas.coaching.exception.FeeAlreadyExistsException;
import com.saas.coaching.exception.InvalidFeePaymentException;
import com.saas.coaching.exception.ResourceNotFoundException;
import com.saas.coaching.repository.FeeRepository;
import com.saas.coaching.repository.InstituteRepository;
import com.saas.coaching.repository.StudentRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeeService {

    private final FeeRepository feeRepository;
    private final StudentRepository studentRepository;
    private final InstituteRepository instituteRepository;

    @Transactional
    public FeeResponseDTO createFee(Long instituteId, CreateFeeRequestDTO request) {
        Institute institute = findInstituteById(instituteId);
        Student student = findStudentByInstitute(request.getStudentId(), instituteId);
        validateDuplicateFee(instituteId, student.getId(), request.getFeeMonth(), request.getFeeYear(), null);

        BigDecimal paidAmount = normalizeAmount(request.getPaidAmount());
        validateAmounts(request.getTotalAmount(), paidAmount);

        Fee fee = Fee.builder()
                .student(student)
                .institute(institute)
                .feeMonth(request.getFeeMonth())
                .feeYear(request.getFeeYear())
                .totalAmount(request.getTotalAmount())
                .paidAmount(paidAmount)
                .dueDate(request.getDueDate())
                .status(resolveStatus(request.getTotalAmount(), paidAmount, request.getDueDate()))
                .remarks(request.getRemarks())
                .build();

        return mapToResponse(feeRepository.save(fee));
    }

    @Transactional(readOnly = true)
    public List<FeeResponseDTO> getAllFees(Long instituteId) {
        findInstituteById(instituteId);
        return feeRepository.findAllByInstituteIdOrderByDueDateAscCreatedAtDesc(instituteId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public FeeResponseDTO getFeeById(Long instituteId, Long feeId) {
        return mapToResponse(findFeeByInstitute(feeId, instituteId));
    }

    @Transactional(readOnly = true)
    public List<FeeResponseDTO> getFeesByStudent(Long instituteId, Long studentId) {
        findStudentByInstitute(studentId, instituteId);
        return feeRepository.findAllByInstituteIdAndStudentIdOrderByFeeYearDescFeeMonthDesc(instituteId, studentId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public FeeResponseDTO updateFee(Long instituteId, Long feeId, UpdateFeeRequestDTO request) {
        Fee fee = findFeeByInstitute(feeId, instituteId);
        validateDuplicateFee(instituteId, fee.getStudent().getId(), request.getFeeMonth(), request.getFeeYear(), feeId);
        validateAmounts(request.getTotalAmount(), fee.getPaidAmount());

        fee.setFeeMonth(request.getFeeMonth());
        fee.setFeeYear(request.getFeeYear());
        fee.setTotalAmount(request.getTotalAmount());
        fee.setDueDate(request.getDueDate());
        fee.setRemarks(request.getRemarks());
        fee.setStatus(resolveStatus(fee.getTotalAmount(), fee.getPaidAmount(), fee.getDueDate()));

        return mapToResponse(feeRepository.save(fee));
    }

    @Transactional
    public FeeResponseDTO recordPayment(Long instituteId, Long feeId, RecordFeePaymentRequestDTO request) {
        Fee fee = findFeeByInstitute(feeId, instituteId);
        BigDecimal paymentAmount = request.getPaymentAmount();
        BigDecimal updatedPaidAmount = fee.getPaidAmount().add(paymentAmount);

        validateAmounts(fee.getTotalAmount(), updatedPaidAmount);

        fee.setPaidAmount(updatedPaidAmount);
        fee.setStatus(resolveStatus(fee.getTotalAmount(), fee.getPaidAmount(), fee.getDueDate()));

        if (request.getRemarks() != null && !request.getRemarks().isBlank()) {
            fee.setRemarks(request.getRemarks());
        }

        return mapToResponse(feeRepository.save(fee));
    }

    private Institute findInstituteById(Long instituteId) {
        return instituteRepository.findById(instituteId)
                .orElseThrow(() -> new ResourceNotFoundException("Institute not found"));
    }

    private Student findStudentByInstitute(Long studentId, Long instituteId) {
        return studentRepository.findByIdAndInstituteId(studentId, instituteId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found for the current institute"));
    }

    private Fee findFeeByInstitute(Long feeId, Long instituteId) {
        return feeRepository.findByIdAndInstituteId(feeId, instituteId)
                .orElseThrow(() -> new ResourceNotFoundException("Fee record not found for the current institute"));
    }

    private void validateDuplicateFee(
            Long instituteId,
            Long studentId,
            Integer feeMonth,
            Integer feeYear,
            Long currentFeeId) {
        boolean exists = currentFeeId == null
                ? feeRepository.existsByInstituteIdAndStudentIdAndFeeMonthAndFeeYear(
                        instituteId, studentId, feeMonth, feeYear)
                : feeRepository.existsByInstituteIdAndStudentIdAndFeeMonthAndFeeYearAndIdNot(
                        instituteId, studentId, feeMonth, feeYear, currentFeeId);

        if (exists) {
            throw new FeeAlreadyExistsException(
                    "A fee record already exists for this student for the selected month and year");
        }
    }

    private void validateAmounts(BigDecimal totalAmount, BigDecimal paidAmount) {
        if (paidAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidFeePaymentException("Paid amount cannot be negative");
        }
        if (paidAmount.compareTo(totalAmount) > 0) {
            throw new InvalidFeePaymentException("Paid amount cannot be greater than total amount");
        }
    }

    private BigDecimal normalizeAmount(BigDecimal amount) {
        return amount == null ? BigDecimal.ZERO : amount;
    }

    private FeeStatus resolveStatus(BigDecimal totalAmount, BigDecimal paidAmount, LocalDate dueDate) {
        if (paidAmount.compareTo(totalAmount) >= 0) {
            return FeeStatus.PAID;
        }
        if (paidAmount.compareTo(BigDecimal.ZERO) > 0) {
            return FeeStatus.PARTIALLY_PAID;
        }
        if (dueDate.isBefore(LocalDate.now())) {
            return FeeStatus.OVERDUE;
        }
        return FeeStatus.PENDING;
    }

    private FeeResponseDTO mapToResponse(Fee fee) {
        return FeeResponseDTO.builder()
                .id(fee.getId())
                .studentId(fee.getStudent().getId())
                .studentName(fee.getStudent().getFullName())
                .instituteId(fee.getInstitute().getId())
                .feeMonth(fee.getFeeMonth())
                .feeYear(fee.getFeeYear())
                .totalAmount(fee.getTotalAmount())
                .paidAmount(fee.getPaidAmount())
                .balanceAmount(fee.getTotalAmount().subtract(fee.getPaidAmount()))
                .dueDate(fee.getDueDate())
                .status(fee.getStatus())
                .remarks(fee.getRemarks())
                .createdAt(fee.getCreatedAt())
                .updatedAt(fee.getUpdatedAt())
                .build();
    }
}
