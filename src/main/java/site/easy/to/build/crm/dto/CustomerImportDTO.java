package site.easy.to.build.crm.dto;

public class CustomerImportDTO {

    private String customer_email;
    private String customer_name;
    private Integer numLigne;

    public CustomerImportDTO() {}

    public CustomerImportDTO(String customer_email, String customer_name, Integer numLigne) {
        this.customer_email = customer_email;
        this.customer_name = customer_name;
        this.numLigne = numLigne;
    }


    public CustomerImportDTO(String customer_email, String customer_name) {
        this.customer_email = customer_email;
        this.customer_name = customer_name;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public Integer getNumLigne() {
        return numLigne;
    }

    public void setNumLigne(Integer numLigne) {
        this.numLigne = numLigne;
    }
}