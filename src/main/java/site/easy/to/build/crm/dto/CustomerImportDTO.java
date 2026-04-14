package site.easy.to.build.crm.dto;

public class CustomerImportDTO {

    private String customer_email;
    private String customer_name;
    private Integer num_ligne;

    public Integer getNum_ligne() {
        return num_ligne;
    }

    public void setNum_ligne(Integer num_ligne) {
        this.num_ligne = num_ligne;
    }

    public CustomerImportDTO() {}

    public CustomerImportDTO(String customer_email, String customer_name, Integer num_ligne) {
        this.customer_email = customer_email;
        this.customer_name = customer_name;
        this.num_ligne = num_ligne;
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

}