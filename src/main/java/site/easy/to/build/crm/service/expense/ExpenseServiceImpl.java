package site.easy.to.build.crm.service.expense;

import org.springframework.stereotype.Service;
import java.util.*;
import site.easy.to.build.crm.dto.CustomerImportDTO;
import site.easy.to.build.crm.dto.ExpenseImportDTO;
import site.easy.to.build.crm.dto.ImportError;
import site.easy.to.build.crm.dto.ValidationResult;
import site.easy.to.build.crm.service.customer.CustomerService;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final CustomerService customerService;

    public ExpenseServiceImpl(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public List<ImportError> isDataValid(ExpenseImportDTO expenseImportDTO , ValidationResult <CustomerImportDTO> customerValidationResult) {
        List <ImportError> errors = new ArrayList<>();

        List <ImportError> customerErrors = customerService.isCustomerValid(customerValidationResult, expenseImportDTO.getCustomer_email(), "EXPENSE", expenseImportDTO.getNumLigne());
        errors.addAll(customerErrors);

        if (expenseImportDTO.getExpense() == null || expenseImportDTO.getExpense() <= 0) {
            errors.add(new ImportError("EXPENSE", expenseImportDTO.getNumLigne(), "Amount must be a positive number : " + expenseImportDTO.getExpense()));
        }

        if (expenseImportDTO.getSubject_or_name() == null || expenseImportDTO.getSubject_or_name().isEmpty()) {
            errors.add(new ImportError("EXPENSE", expenseImportDTO.getNumLigne(), "Subject or Name is required"));
        }
    
        if (expenseImportDTO.getType() == null || expenseImportDTO.getType().isEmpty()) {
            errors.add(new ImportError("EXPENSE", expenseImportDTO.getNumLigne(), "Type is required"));
        }

        String status = expenseImportDTO.getType().toLowerCase();

        if (expenseImportDTO.getType() == "Ticket") {
            String [] validStatus = {"open", "assigned", "on-hold", "in-progress", "resolved", "closed", "reopened", "pending-customer-response", "escalated", "archived"};

            if (!Arrays.asList(validStatus).contains(status)) {
                 errors.add(new ImportError("EXPENSE_TICKET", expenseImportDTO.getNumLigne(), "Status must be either 'open', 'assigned', 'on-hold', 'in-progress', 'resolved', 'closed', 'reopened', 'pending-customer-response', 'escalated' or 'archived' , but found : " + status));
            }
        } else if (expenseImportDTO.getType() == "Lead") {
            String [] validStatus = {"meeting-to-shedule","scheduled","archived","success","assign-to-sales"};

            if (!Arrays.asList(validStatus).contains(status)) {
                 errors.add(new ImportError("EXPENSE_LEAD", expenseImportDTO.getNumLigne(), "Status must be either 'meeting-to-shedule', 'scheduled', 'archived', 'success' or 'assign-to-sales' , but found : " + status));
            }
        } else {
            errors.add(new ImportError("EXPENSE", expenseImportDTO.getNumLigne(), "Type must be either 'Ticket' or 'Lead'"));
        }

        return errors;
    }


    @Override
    public ValidationResult<ExpenseImportDTO> validateExpenseImportData(List<ExpenseImportDTO> expenseImportDTOList, ValidationResult <CustomerImportDTO> customerValidationResult) {
        ValidationResult<ExpenseImportDTO> validationResult = new ValidationResult<>();

        for (ExpenseImportDTO expenseImportDTO : expenseImportDTOList) {
            List<ImportError> errors = isDataValid(expenseImportDTO, customerValidationResult);
            if (errors.isEmpty()) {
                validationResult.getValidItems().add(expenseImportDTO);
            } else {
                validationResult.getInvalidItems().add(expenseImportDTO);
                validationResult.getErrors().addAll(errors);
            }
        }

        validationResult.setTotalItems(expenseImportDTOList.size());
        validationResult.setValidItemCount(validationResult.getValidItems().size());
        validationResult.setInvalidItemCount(validationResult.getInvalidItems().size());
        validationResult.setNomTable("EXPENSE");

        return validationResult;
    }
    
}
