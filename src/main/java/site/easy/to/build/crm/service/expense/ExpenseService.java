package site.easy.to.build.crm.service.expense;
import site.easy.to.build.crm.dto.CustomerImportDTO;
import site.easy.to.build.crm.dto.ExpenseImportDTO;
import site.easy.to.build.crm.dto.ImportError;
import site.easy.to.build.crm.dto.ValidationResult;

import java.util.List;

public interface ExpenseService {

    List<ImportError> isDataValid(ExpenseImportDTO expenseImportDTO , ValidationResult <CustomerImportDTO> customerValidationResult);
    ValidationResult <ExpenseImportDTO> validateExpenseImportData(List<ExpenseImportDTO> expenseImportDTOList,ValidationResult <CustomerImportDTO> customerValidationResult);
    
}
