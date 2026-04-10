package site.easy.to.build.crm.service.budget;

import site.easy.to.build.crm.dto.BudgetImportDTO;
import site.easy.to.build.crm.dto.CustomerImportDTO;
import site.easy.to.build.crm.dto.ImportError;
import site.easy.to.build.crm.dto.ValidationResult;
import site.easy.to.build.crm.entity.Budget;

import java.util.List;

public interface BudgetService {

    public List<Budget> findAll();
    public List<Budget> getCustomerBudgets(int customerId);
    public Budget save(Budget budget);
    public void delete(Budget budget);
    public long countByCustomerId(int customerId);

    public List <ImportError> isDataValid (BudgetImportDTO budget, ValidationResult<CustomerImportDTO> customerValidationResult);
    public ValidationResult<BudgetImportDTO> validateBudgetImportData(List<BudgetImportDTO> budgetImportDTOList, ValidationResult<CustomerImportDTO> customerValidationResult);

    public Budget transformDTOtoEntity(BudgetImportDTO budgetImportDTO);

    public BudgetImportDTO transformEntityToDTO(Budget b);

}
