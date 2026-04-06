package site.easy.to.build.crm.service.budget;

import org.springframework.stereotype.Service;

import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.repository.BudgetRepository;
import site.easy.to.build.crm.repository.CustomerRepository;
import site.easy.to.build.crm.repository.BudgetRepository;

import java.util.List;

@Service
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final CustomerRepository customerRepository;

    public BudgetServiceImpl(BudgetRepository budgetRepository, CustomerRepository customerRepository) {
        this.budgetRepository = budgetRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Budget> findAll() {
        return budgetRepository.findAll();
    }

    @Override
    public List<Budget> getCustomerBudgets(int customerId) {
        return budgetRepository.findByCustomerCustomerId(customerId);
    }

    @Override
    public Budget save(Budget budget) {
        Customer c = budget.getCustomer();
        c.setBudget(c.getBudget().add(budget.getAmount()));
        customerRepository.save(c);
        budgetRepository.save(budget);
        return budget;
    }

    @Override
    public void delete(Budget budget) {
        budgetRepository.delete(budget);
    }

    @Override
    public long countByCustomerId(int customerId) {
        return budgetRepository.countByCustomerCustomerId(customerId);
    }
}
