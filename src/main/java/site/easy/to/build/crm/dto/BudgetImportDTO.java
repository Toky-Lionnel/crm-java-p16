package site.easy.to.build.crm.dto;

public class BudgetImportDTO {
    private String customer_email;
    private Double budget;
    private Integer num_ligne;

    public Integer getNum_ligne() {
        return num_ligne;
    }

    public void setNum_ligne(Integer num_ligne) {
        this.num_ligne = num_ligne;
    }

    public BudgetImportDTO() {}

    public BudgetImportDTO(String customer_email, Double budget, Integer num_ligne) {
        this.customer_email = customer_email;
        this.budget = budget;
        this.num_ligne = num_ligne;
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

    
}