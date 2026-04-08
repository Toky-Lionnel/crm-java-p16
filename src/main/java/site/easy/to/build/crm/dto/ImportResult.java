package site.easy.to.build.crm.dto;
import site.easy.to.build.crm.dto.BudgetImportDTO;
import site.easy.to.build.crm.dto.CustomerImportDTO;
import site.easy.to.build.crm.dto.ExpenseImportDTO;
import java.util.List;

public class ImportResult {
    
    private List<CustomerImportDTO> customers;
    private List<BudgetImportDTO> budgets;
    private List<ExpenseImportDTO> expenses;

    public ImportResult() {
    }

    public ImportResult(List<CustomerImportDTO> customers, List<BudgetImportDTO> budgets, List<ExpenseImportDTO> expenses) {
        this.customers = customers;
        this.budgets = budgets;
        this.expenses = expenses;
    }

    public List<CustomerImportDTO> getCustomers() {
        return customers;
    }

    public void setCustomers(List<CustomerImportDTO> customers) {
        this.customers = customers;
    }

    public List<BudgetImportDTO> getBudgets() {
        return budgets;
    }

    public void setBudgets(List<BudgetImportDTO> budgets) {
        this.budgets = budgets;
    }

    public List<ExpenseImportDTO> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<ExpenseImportDTO> expenses) {
        this.expenses = expenses;
    }

    @Override
    public String toString() {

        String result = "";

        for (CustomerImportDTO c : customers) {
            result += "Customer: " + c.getCustomer_email() + ", " + c.getCustomer_name() + ", " + c.getNumLigne() + "\n";
        }

        for (BudgetImportDTO b : budgets) {
            result += "Budget: " + b.getBudget() + ", " + b.getCustomer_email() + "," + b.getNumLigne() + "\n";
        }

        for (ExpenseImportDTO e : expenses) {
            result += "Expense: " + e.getType() + ", " + e.getExpense() + ", " + e.getNumLigne() + "\n";
        }

        return result;
    }


}
