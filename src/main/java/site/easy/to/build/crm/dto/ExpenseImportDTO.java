package site.easy.to.build.crm.dto;

public class ExpenseImportDTO {

    
    private String customer_email;
    private String subject_or_name;
    private String type;
    private String status;
    private Double expense;
    private Integer numLigne;
    
    public String getCreatedat() {
        return createdat;
    }

    public void setCreatedat(String createdat) {
        this.createdat = createdat;
    }

    private String createdat;

    

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public String getSubject_or_name() {
        return subject_or_name;
    }

    public void setSubject_or_name(String subject_or_name) {
        this.subject_or_name = subject_or_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getExpense() {
        return expense;
    }

    public void setExpense(Double expense) {
        this.expense = expense;
    }

    public Integer getNumLigne() {
        return numLigne;
    }

    public void setNumLigne(Integer numLigne) {
        this.numLigne = numLigne;
    }

    public ExpenseImportDTO() {}

    public ExpenseImportDTO(String customer_email, String subject_or_name, String type, String status, Double expense, Integer numLigne) {
        this.customer_email = customer_email;
        this.subject_or_name = subject_or_name;
        this.type = type;
        this.status = status;
        this.expense = expense;
        this.numLigne = numLigne;
    }
}