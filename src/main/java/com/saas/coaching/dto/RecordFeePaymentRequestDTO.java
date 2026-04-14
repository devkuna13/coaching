package com.saas.coaching.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;

@Data
@Schema(name = "RecordFeePaymentRequest", description = "Request payload to record a payment against a fee record")
public class RecordFeePaymentRequestDTO {

    @Schema(description = "Payment amount being received", example = "1000.00")
    @NotNull
    @DecimalMin(value = "0.01", message = "Payment amount must be greater than zero")
    private BigDecimal paymentAmount;

    @Schema(description = "Optional payment remarks", example = "Cash collected at front desk")
    @Size(max = 500)
    private String remarks;
}
