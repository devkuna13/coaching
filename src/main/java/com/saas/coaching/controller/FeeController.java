package com.saas.coaching.controller;

import com.saas.coaching.dto.CreateFeeRequestDTO;
import com.saas.coaching.dto.FeeResponseDTO;
import com.saas.coaching.dto.RecordFeePaymentRequestDTO;
import com.saas.coaching.dto.UpdateFeeRequestDTO;
import com.saas.coaching.security.CustomUserPrincipal;
import com.saas.coaching.service.FeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/fees")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Fees", description = "Institute-scoped fee tracking and payment APIs")
public class FeeController {

    private final FeeService feeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create fee", description = "Creates a fee record for a student under the authenticated institute")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Fee created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "401", description = "Authentication token is missing, invalid, or expired"),
            @ApiResponse(responseCode = "404", description = "Student not found for the current institute"),
            @ApiResponse(responseCode = "409", description = "Fee for this student and month already exists")
    })
    public FeeResponseDTO createFee(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @Valid @RequestBody CreateFeeRequestDTO request) {
        return feeService.createFee(principal.getInstituteId(), request);
    }

    @GetMapping
    @Operation(summary = "List fees", description = "Returns all fee records for the authenticated institute")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fees fetched successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication token is missing, invalid, or expired")
    })
    public List<FeeResponseDTO> getAllFees(@AuthenticationPrincipal CustomUserPrincipal principal) {
        return feeService.getAllFees(principal.getInstituteId());
    }

    @GetMapping("/{feeId}")
    @Operation(summary = "Get fee by id", description = "Returns a single fee record for the authenticated institute")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fee fetched successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication token is missing, invalid, or expired"),
            @ApiResponse(responseCode = "404", description = "Fee record not found for the current institute")
    })
    public FeeResponseDTO getFeeById(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable Long feeId) {
        return feeService.getFeeById(principal.getInstituteId(), feeId);
    }

    @PutMapping("/{feeId}")
    @Operation(summary = "Update fee", description = "Updates a fee record for the authenticated institute")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fee updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "401", description = "Authentication token is missing, invalid, or expired"),
            @ApiResponse(responseCode = "404", description = "Fee record not found for the current institute"),
            @ApiResponse(responseCode = "409", description = "Fee for this student and month already exists")
    })
    public FeeResponseDTO updateFee(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable Long feeId,
            @Valid @RequestBody UpdateFeeRequestDTO request) {
        return feeService.updateFee(principal.getInstituteId(), feeId, request);
    }

    @PostMapping("/{feeId}/pay")
    @Operation(summary = "Record fee payment", description = "Records a payment against a fee record and recalculates fee status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment recorded successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed or payment amount is invalid"),
            @ApiResponse(responseCode = "401", description = "Authentication token is missing, invalid, or expired"),
            @ApiResponse(responseCode = "404", description = "Fee record not found for the current institute")
    })
    public FeeResponseDTO recordPayment(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable Long feeId,
            @Valid @RequestBody RecordFeePaymentRequestDTO request) {
        return feeService.recordPayment(principal.getInstituteId(), feeId, request);
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "List fees by student", description = "Returns all fee records for a student under the authenticated institute")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Student fee records fetched successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication token is missing, invalid, or expired"),
            @ApiResponse(responseCode = "404", description = "Student not found for the current institute")
    })
    public List<FeeResponseDTO> getFeesByStudent(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable Long studentId) {
        return feeService.getFeesByStudent(principal.getInstituteId(), studentId);
    }
}
