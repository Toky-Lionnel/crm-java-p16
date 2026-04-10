package site.easy.to.build.crm.dto;

import java.util.List;

public class ExportDataDTO {

    private CustomerImportDTO customerImportDTO;
    private List <ExpenseImportDTO> expenseImportDTOList;
    private List <BudgetImportDTO> budgetImportDTOList;

    public List<BudgetImportDTO> getBudgetImportDTOList() {
        return budgetImportDTOList;
    }

    public void setBudgetImportDTOList(List<BudgetImportDTO> budgetImportDTOList) {
        this.budgetImportDTOList = budgetImportDTOList;
    }

    public ExportDataDTO(CustomerImportDTO customerImportDTO, List<ExpenseImportDTO> expenseImportDTOList, List<BudgetImportDTO> budgetImportDTOList) {
        this.customerImportDTO = customerImportDTO;
        this.expenseImportDTOList = expenseImportDTOList;
        this.budgetImportDTOList = budgetImportDTOList;
    }

    public CustomerImportDTO getCustomerImportDTO() {
        return customerImportDTO;
    }

    public void setCustomerImportDTO(CustomerImportDTO customerImportDTO) {
        this.customerImportDTO = customerImportDTO;
    }

    public List<ExpenseImportDTO> getExpenseImportDTOList() {
        return expenseImportDTOList;
    }

    public void setExpenseImportDTOList(List<ExpenseImportDTO> expenseImportDTOList) {
        this.expenseImportDTOList = expenseImportDTOList;
    }

    
    
}
