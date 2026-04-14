package com.saas.coaching.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
@Schema(name = "UpdateFeeRequest", description = "Request payload to update fee details for a student")
public class UpdateFeeRequestDTO {

    @Schema(description = "Fee month in numeric format", example = "4", minimum = "1", maximum = "12")
    @NotNull
    @Min(1)
    @Max(12)
    private Integer feeMonth;

    @Schema(description = "Fee year", example = "2026")
    @NotNull
    @Min(2000)
    @Max(2100)
    private Integer feeYear;

    @Schema(description = "Total fee amount for the month", example = "2500.00")
    @NotNull
    @DecimalMin(value = "0.01", message = "Total amount must be greater than zero")
    private BigDecimal totalAmount;

    @Schema(description = "Fee due date", example = "2026-04-10")
    @NotNull
    private LocalDate dueDate;

    @Schema(description = "Optional notes for the fee record", example = "Updated April fee")
    @Size(max = 500)
    private String remarks;
}
