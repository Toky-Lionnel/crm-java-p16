package site.easy.to.build.crm.service.budget;

import site.easy.to.build.crm.entity.Budget;

import java.util.List;

public interface BudgetService {

    public List<Budget> findAll();
    public List<Budget> getCustomerBudgets(int customerId);
    public Budget save(Budget budget);
    public void delete(Budget budget);
    public long countByCustomerId(int customerId);

}
