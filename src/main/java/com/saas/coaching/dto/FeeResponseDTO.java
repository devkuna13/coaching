package com.saas.coaching.dto;

import com.saas.coaching.entity.FeeStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(name = "FeeResponse", description = "Fee details returned by the API")
public class FeeResponseDTO {

    @Schema(description = "Unique fee record identifier", example = "1")
    private Long id;

    @Schema(description = "Student id linked to this fee", example = "1")
    private Long studentId;

    @Schema(description = "Student full name", example = "Rahul Sharma")
    private String studentName;

    @Schema(description = "Institute id owning this fee record", example = "1")
    private Long instituteId;

    @Schema(description = "Fee month in numeric format", example = "4")
    private Integer feeMonth;

    @Schema(description = "Fee year", example = "2026")
    private Integer feeYear;

    @Schema(description = "Total fee amount", example = "2500.00")
    private BigDecimal totalAmount;

    @Schema(description = "Amount paid so far", example = "1000.00")
    private BigDecimal paidAmount;

    @Schema(description = "Remaining balance amount", example = "1500.00")
    private BigDecimal balanceAmount;

    @Schema(description = "Fee due date", example = "2026-04-10")
    private LocalDate dueDate;

    @Schema(description = "Fee collection status", allowableValues = {"PENDING", "PARTIALLY_PAID", "PAID", "OVERDUE"}, example = "PARTIALLY_PAID")
    private FeeStatus status;

    @Schema(description = "Optional fee remarks", example = "April tuition fee")
    private String remarks;

    @Schema(description = "Fee record creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Fee record last update timestamp")
    private LocalDateTime updatedAt;
}
