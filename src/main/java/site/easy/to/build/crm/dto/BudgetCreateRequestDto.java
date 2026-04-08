package site.easy.to.build.crm.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class BudgetCreateRequestDto {
    @NotNull(message = "Amount is required")
    @Digits(integer = 10, fraction = 2, message = "Amount must be a valid number with up to 2 decimal places")
    @DecimalMin(value = "0.00", inclusive = true, message = "Amount must be greater than or equal to 0.00")
    @DecimalMax(value = "9999999.99", inclusive = true, message = "Amount must be less than or equal to 9999999.99")
    private BigDecimal amount;

    @NotNull(message = "Customer id is required")
    private Integer customerId;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
}