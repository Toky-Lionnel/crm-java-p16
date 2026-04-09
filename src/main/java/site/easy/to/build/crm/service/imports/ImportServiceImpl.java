package site.easy.to.build.crm.service.imports;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.dto.BudgetImportDTO;
import site.easy.to.build.crm.dto.CustomerImportDTO;
import site.easy.to.build.crm.dto.ExpenseImportDTO;
import site.easy.to.build.crm.dto.ImportError;
import site.easy.to.build.crm.dto.ImportData;
import site.easy.to.build.crm.dto.ImportRequest;
import site.easy.to.build.crm.dto.ValidationResult;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.expense.ExpenseService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImportServiceImpl implements ImportService {

    private final CustomerService customerService;
    private final BudgetService budgetService;
    private final ExpenseService expenseService;
    private final TicketService ticketService;
    private final LeadService leadService;

    public ImportServiceImpl(CustomerService customerService, BudgetService budgetService, ExpenseService expenseService, TicketService ticketService, LeadService leadService) {
        this.customerService = customerService;
        this.budgetService = budgetService;
        this.expenseService = expenseService;
        this.ticketService = ticketService;
        this.leadService = leadService;
    }

    @Override
    public List<ImportRequest> parseJson(String json) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(
                json,
                mapper.getTypeFactory().constructCollectionType(List.class, ImportRequest.class));
    }


    private <T> List<T> convertData(JsonNode dataNode, Class<T> clazz, ObjectMapper mapper) {
        List<T> result = new ArrayList<>();

        if (dataNode.isArray()) {
            for (JsonNode node : dataNode) {
                result.add(mapper.convertValue(node, clazz));
            }
        } else {
            result.add(mapper.convertValue(dataNode, clazz));
        }

        return result;
    }


    public List<ImportData> transformData(List<ImportRequest> requests) {
        ObjectMapper mapper = new ObjectMapper();

        List<CustomerImportDTO> customers = new ArrayList<>();
        List<BudgetImportDTO> budgets = new ArrayList<>();
        List<ExpenseImportDTO> expenses = new ArrayList<>();

        List<ImportData> result = new ArrayList<>();

        for (ImportRequest req : requests) {

            switch (req.getTable_name()) {

                case "CUSTOMER":
                    customers.addAll(
                            convertData(req.getData(), CustomerImportDTO.class, mapper));
                    result.add(new ImportData<CustomerImportDTO>(req.getFile_name(), customers, req.getTable_name()));
                    break;

                case "BUDGET":
                    budgets.addAll(
                            convertData(req.getData(), BudgetImportDTO.class, mapper));
                    result.add(new ImportData<BudgetImportDTO>(req.getFile_name(), budgets, req.getTable_name()));
                    break;

                case "EXPENSE":
                    expenses.addAll(
                            convertData(req.getData(), ExpenseImportDTO.class, mapper));
                    result.add(new ImportData<ExpenseImportDTO>(req.getFile_name(), expenses, req.getTable_name()));
                    break;
            }
        }
        return result;
    }


    @SuppressWarnings("unchecked")
    private ValidationResult <CustomerImportDTO> generateCustomerReference (List <ImportData> importDatas) {
        ValidationResult<CustomerImportDTO> result = new ValidationResult<>();
        for (ImportData t : importDatas) {
            if (t.getTable_name().equalsIgnoreCase("customer")) {
                result = customerService.validateCustomerImportData((List <CustomerImportDTO>)t.getData());
                break;
            }
        }
        return result;
    }


    @SuppressWarnings("unchecked")
    private List<ValidationResult> validateDataFileName(List<ImportData> importDatas) {

        List <ValidationResult> result = new ArrayList();
        ValidationResult<CustomerImportDTO> customerValidationResult = generateCustomerReference(importDatas);
        result.add(customerValidationResult);

        for (ImportData t : importDatas) {
            if (t.getTable_name().equalsIgnoreCase("budget")) {
                
                ValidationResult budget = budgetService.validateBudgetImportData((List <BudgetImportDTO>)t.getData(),customerValidationResult);
                budget.setFileName(t.getFile_name());
                result.add(budget);
            }

            else if (t.getTable_name().equalsIgnoreCase("expense")) {
                ValidationResult expense = expenseService.validateExpenseImportData((List <ExpenseImportDTO>)t.getData(),customerValidationResult);
                expense.setFileName(t.getFile_name());
                result.add(expense);
            }
        }

        return result;
    }


    @SuppressWarnings("unchecked")
	private String afficherResultat (List <ValidationResult> validationResults) {
        StringBuilder sb = new StringBuilder();
        for (ValidationResult vr : validationResults) {
            sb.append("Table: ").append(vr.getNomTable()).append("\n");
            sb.append("File: ").append(vr.getFileName()).append("\n");
            sb.append("Total items: ").append(vr.getTotalItems()).append("\n");
            sb.append("Valid items: ").append(vr.getValidItemCount()).append("\n");
            sb.append("Invalid items: ").append(vr.getInvalidItemCount()).append("\n");
            sb.append("Errors:\n");
            for (ImportError error : (List<ImportError>)vr.getErrors()) {
                sb.append("- Line ").append(error.getLine()).append(": ").append(error.getMessage()).append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    
    @SuppressWarnings("unchecked")
	private void saveValidData(List<ValidationResult> validationResults) {
        for (ValidationResult vr : validationResults) {
            switch (vr.getNomTable()) {
                case "CUSTOMER":
                    for (CustomerImportDTO customerDTO : ((ValidationResult<CustomerImportDTO>) vr).getValidItems()) {
                        customerService.save(customerService.transformDTOtoEntity(customerDTO));
                    }
                    break;
                case "BUDGET":
                    for (BudgetImportDTO budgetDTO : ((ValidationResult<BudgetImportDTO>) vr).getValidItems()) {
                        budgetService.save(budgetService.transformDTOtoEntity(budgetDTO));
                    }
                    break;
                case "EXPENSE":
                    for (ExpenseImportDTO expenseDTO : ((ValidationResult<ExpenseImportDTO>) vr).getValidItems()) {
                        if (expenseDTO.getType().equalsIgnoreCase("Ticket")) {
                            ticketService.save(expenseService.transformExpenseDTOToTicket(expenseDTO));
                        } else if (expenseDTO.getType().equalsIgnoreCase("Lead")) {
                            leadService.save(expenseService.transformExpenseDTOToLead(expenseDTO));
                        }
                    }
            }
        }
    }

    @Override
    public String processImport(String json) throws Exception {
        try {

            List<ImportRequest> requests = parseJson(json);
            List <ImportData> data = transformData(requests);

            List<ValidationResult> validationResults = validateDataFileName(data);
            
            boolean hasErrors = false;
            for (ValidationResult vr : validationResults) {
                if (!vr.getErrors().isEmpty()) {
                    hasErrors = true;
                    break;
                }
            }

            if (!hasErrors) {
                saveValidData(validationResults);
                return "Import successful! All data has been validated and saved.";
            } 

            return afficherResultat(validationResults);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing import: " + e.getMessage();
        }
    }

}
