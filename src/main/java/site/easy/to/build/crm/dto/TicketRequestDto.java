package site.easy.to.build.crm.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class TicketRequestDto {
    @NotBlank(message = "Subject is required")
    private String subject;

    private String description;

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "^(open|assigned|on-hold|in-progress|resolved|closed|reopened|pending-customer-response|escalated|archived)$", message = "Invalid status")
    private String status;

    @NotBlank(message = "Priority is required")
    @Pattern(regexp = "^(low|medium|high|closed|urgent|critical)$", message = "Invalid priority")
    private String priority;

    @NotNull(message = "Amount is required")
    @Digits(integer = 10, fraction = 2, message = "Amount must be a valid number with up to 2 decimal places")
    @DecimalMin(value = "0.00", inclusive = true, message = "Amount must be greater than or equal to 0.00")
    @DecimalMax(value = "9999999.99", inclusive = true, message = "Amount must be less than or equal to 9999999.99")
    private BigDecimal amount;

    @NotNull(message = "Employee id is required")
    private Integer employeeId;

    @NotNull(message = "Customer id is required")
    private Integer customerId;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
}
