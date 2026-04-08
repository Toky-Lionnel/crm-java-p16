package site.easy.to.build.crm.service.imports;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import site.easy.to.build.crm.dto.BudgetImportDTO;
import site.easy.to.build.crm.dto.CustomerImportDTO;
import site.easy.to.build.crm.dto.ExpenseImportDTO;
import site.easy.to.build.crm.dto.ImportRequest;
import site.easy.to.build.crm.dto.ImportResult;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImportServiceImpl implements ImportService {

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


    @Override
    public ImportResult processImport(List<ImportRequest> requests) {
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

}
