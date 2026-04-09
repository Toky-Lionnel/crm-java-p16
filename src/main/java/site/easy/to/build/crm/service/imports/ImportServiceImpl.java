package site.easy.to.build.crm.service.imports;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import site.easy.to.build.crm.dto.BudgetImportDTO;
import site.easy.to.build.crm.dto.CustomerImportDTO;
import site.easy.to.build.crm.dto.ExpenseImportDTO;
import site.easy.to.build.crm.dto.ImportError;
import site.easy.to.build.crm.dto.ImportRequest;
import site.easy.to.build.crm.dto.ImportResult;
import site.easy.to.build.crm.dto.ValidationResult;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.customer.CustomerService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImportServiceImpl implements ImportService {

    private final CustomerService customerService;
    private final BudgetService budgetService;

    public ImportServiceImpl(CustomerService customerService, BudgetService budgetService) {
        this.customerService = customerService;
        this.budgetService = budgetService;
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


    public ImportResult transformData(List<ImportRequest> requests) {
        ObjectMapper mapper = new ObjectMapper();

        List<CustomerImportDTO> customers = new ArrayList<>();
        List<BudgetImportDTO> budgets = new ArrayList<>();
        List<ExpenseImportDTO> expenses = new ArrayList<>();

        for (ImportRequest req : requests) {

            switch (req.getTable_name()) {

                case "CUSTOMER":
                    customers.addAll(
                            convertData(req.getData(), CustomerImportDTO.class, mapper));
                    break;

                case "BUDGET":
                    budgets.addAll(
                            convertData(req.getData(), BudgetImportDTO.class, mapper));
                    break;

                case "EXPENSE":
                    expenses.addAll(
                            convertData(req.getData(), ExpenseImportDTO.class, mapper));
                    break;
            }
        }
        return new ImportResult(customers, budgets, expenses);
    }

    private List<ValidationResult> validateData(ImportResult importResult) {
        List <ValidationResult> result = new ArrayList<>();
        ValidationResult<CustomerImportDTO> customerValidationResult = customerService.validateCustomerImportData(importResult.getCustomers());
        ValidationResult<BudgetImportDTO> budgetValidationResult = budgetService.validateBudgetImportData(importResult.getBudgets(), customerValidationResult);
        result.add(customerValidationResult);
        result.add(budgetValidationResult);
        return result;
    }


    private String afficherResultat (List <ValidationResult> validationResults) {
        StringBuilder sb = new StringBuilder();
        for (ValidationResult vr : validationResults) {
            sb.append("Table: ").append(vr.getNomTable()).append("\n");
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

    @Override
    public String processImport(String json) throws Exception {
        try {
            List<ImportRequest> requests = parseJson(json);
            ImportResult importResult = transformData(requests);
            List<ValidationResult> validationResults = validateData(importResult);
            return afficherResultat(validationResults);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing import: " + e.getMessage();
        }
    }

}
