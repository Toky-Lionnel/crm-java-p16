package site.easy.to.build.crm.dto;

public class BudgetImportDTO {
    private String customer_email;
    private Double budget;
    private Integer numLigne;

    public BudgetImportDTO() {}

    public BudgetImportDTO(String customer_email, Double budget, Integer numLigne) {
        this.customer_email = customer_email;
        this.budget = budget;
        this.numLigne = numLigne;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public Integer getNumLigne() {
        return numLigne;
    }

    public void setNumLigne(Integer numLigne) {
        this.numLigne = numLigne;
    }
}