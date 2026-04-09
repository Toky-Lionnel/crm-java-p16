package site.easy.to.build.crm.service.budget;

import org.springframework.stereotype.Service;

import site.easy.to.build.crm.dto.BudgetImportDTO;
import site.easy.to.build.crm.dto.CustomerImportDTO;
import site.easy.to.build.crm.dto.ImportError;
import site.easy.to.build.crm.dto.ValidationResult;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.repository.BudgetRepository;
import site.easy.to.build.crm.repository.CustomerRepository;
import site.easy.to.build.crm.service.customer.CustomerService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final CustomerRepository customerRepository;
    private final CustomerService customerService;

    public BudgetServiceImpl(BudgetRepository budgetRepository, 
        CustomerRepository customerRepository, CustomerService customerService) {
        this.budgetRepository = budgetRepository;
        this.customerRepository = customerRepository;
        this.customerService = customerService;
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

    @Override
    public List<ImportError> isDataValid(BudgetImportDTO budget, ValidationResult <CustomerImportDTO> customerValidationResult) {
        
        List<ImportError> errors = new ArrayList<>();


        if (budget.getBudget() == null || budget.getBudget() <= 0) {
            errors.add(new ImportError("BUDGET", budget.getNumLigne(), "Amount must be a positive number : " + budget.getBudget()));
        }

        List<CustomerImportDTO> validCustomers = customerValidationResult.getValidItems();
        List<CustomerImportDTO> invalidCustomers = customerValidationResult.getInvalidItems();
        String customerEmail = budget.getCustomer_email();

        boolean foundInValidCustomers = validCustomers.stream()
                .anyMatch(c -> Objects.equals(c.getCustomer_email(), customerEmail));
        boolean foundInInvalidCustomers = invalidCustomers.stream()
                .anyMatch(c -> Objects.equals(c.getCustomer_email(), customerEmail));

        if (foundInInvalidCustomers) {
            errors.add(new ImportError("BUDGET", budget.getNumLigne(), "Customer with email " + customerEmail + " is invalid in customer import"));
        }

        if (!foundInValidCustomers && !foundInInvalidCustomers) {
            Customer customer = customerService.findByEmail(customerEmail);
            if (customer == null) {
                errors.add(new ImportError("BUDGET", budget.getNumLigne(), "Customer with email " + customerEmail + " not found in customer import and database"));
            }
        }

        return errors;
    }

    @Override
    public ValidationResult<BudgetImportDTO> validateBudgetImportData(List<BudgetImportDTO> budgetImportDTOList, ValidationResult<CustomerImportDTO> customerValidationResult) {
        ValidationResult<BudgetImportDTO> validationResult = new ValidationResult<>();

        for (BudgetImportDTO budgetImportDTO : budgetImportDTOList) {
            List<ImportError> errors = isDataValid(budgetImportDTO, customerValidationResult);
            if (errors.isEmpty()) {
                validationResult.getValidItems().add(budgetImportDTO);
            } else {
                validationResult.getInvalidItems().add(budgetImportDTO);
                validationResult.getErrors().addAll(errors);
            }
        }

        validationResult.setTotalItems(budgetImportDTOList.size());
        validationResult.setValidItemCount(validationResult.getValidItems().size());
        validationResult.setInvalidItemCount(validationResult.getInvalidItems().size());
        validationResult.setNomTable("BUDGET");
        return validationResult;
    }


    @Override
    public Budget transformDTOtoEntity(BudgetImportDTO budgetImportDTO) {
        Customer customer = customerService.findByEmail(budgetImportDTO.getCustomer_email());
        
        Budget budget = new Budget();
        budget.setAmount(BigDecimal.valueOf(budgetImportDTO.getBudget()));
        budget.setCustomer(customer);
        budget.setCreatedAt(LocalDateTime.now());
        return budget;
    }


}
