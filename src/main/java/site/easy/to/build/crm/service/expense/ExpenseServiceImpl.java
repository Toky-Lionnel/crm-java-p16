package site.easy.to.build.crm.service.expense;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import site.easy.to.build.crm.dto.CustomerImportDTO;
import site.easy.to.build.crm.dto.ExpenseImportDTO;
import site.easy.to.build.crm.dto.ImportError;
import site.easy.to.build.crm.dto.ValidationResult;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.Ticket;
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

        String status = expenseImportDTO.getStatus().toLowerCase();

        if (expenseImportDTO.getType().equalsIgnoreCase("ticket")) {
            String [] validStatus = {"open", "assigned", "on-hold", "in-progress", "resolved", "closed", "reopened", "pending-customer-response", "escalated", "archived"};

            if (!Arrays.asList(validStatus).contains(status)) {
                 errors.add(new ImportError("EXPENSE_TICKET", expenseImportDTO.getNumLigne(), "Status must be either 'open', 'assigned', 'on-hold', 'in-progress', 'resolved', 'closed', 'reopened', 'pending-customer-response', 'escalated' or 'archived' , but found : " + status));
            }
        } else if (expenseImportDTO.getType().equalsIgnoreCase("lead")) {
            String [] validStatus = {"meeting-to-schedule","scheduled","archived","success","assign-to-sales"};

            if (!Arrays.asList(validStatus).contains(status)) {
                 errors.add(new ImportError("EXPENSE_LEAD", expenseImportDTO.getNumLigne(), "Status must be either 'meeting-to-schedule', 'scheduled', 'archived', 'success' or 'assign-to-sales' , but found : " + status));
            }
        } else {
            errors.add(new ImportError("EXPENSE", expenseImportDTO.getNumLigne(), "Type must be either 'Ticket' or 'Lead' but found : " + expenseImportDTO.getType()));
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


    @Override
    public Ticket transformExpenseDTOToTicket(ExpenseImportDTO expenseImportDTO) {
        Customer c = customerService.findByEmail(expenseImportDTO.getCustomer_email());

        Ticket ticket = new Ticket();
        ticket.setSubject(expenseImportDTO.getSubject_or_name());
        ticket.setStatus(expenseImportDTO.getStatus());
        ticket.setCustomer(c);
        ticket.setPriority("critical");
        ticket.setAmount(BigDecimal.valueOf(expenseImportDTO.getExpense()));
        ticket.setCreatedAt(LocalDateTime.now());
        return ticket;
    }


    @Override
    public Lead transformExpenseDTOToLead(ExpenseImportDTO expenseImportDTO) {
        Customer c = customerService.findByEmail(expenseImportDTO.getCustomer_email());

        Lead lead = new Lead();
        lead.setName(expenseImportDTO.getSubject_or_name());
        lead.setStatus(expenseImportDTO.getStatus());
        lead.setCustomer(c);
        lead.setAmount(BigDecimal.valueOf(expenseImportDTO.getExpense()));
        lead.setCreatedAt(LocalDateTime.now());
        return lead;
    }
    
}
