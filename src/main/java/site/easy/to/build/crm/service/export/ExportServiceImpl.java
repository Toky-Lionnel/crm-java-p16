package site.easy.to.build.crm.service.export;

import org.springframework.stereotype.Service;

import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.dto.*;
import site.easy.to.build.crm.entity.*;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.expense.ExpenseService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;
import java.util.List;
import java.util.ArrayList;
import java.util.StringJoiner;

@Service
public class ExportServiceImpl implements ExportService {

    private final CustomerService customerService;
    private final ExpenseService expenseService;
    private final LeadService leadService;
    private final TicketService ticketService;
    private final BudgetService budgetService;


    public ExportServiceImpl(CustomerService customerService, ExpenseService expenseService, 
        LeadService leadService, TicketService ticketService, BudgetService budgetService) {
        this.customerService = customerService;
        this.expenseService = expenseService;
        this.leadService = leadService;
        this.ticketService = ticketService;
        this.budgetService = budgetService;
    }


    @Override
    public ExportDataDTO exportDataToJson(int customerId) {
        Customer c = customerService.findByCustomerId(customerId);

        List <Lead> leads = leadService.getCustomerLeads(customerId);
        List <Ticket> tickets = ticketService.findCustomerTickets(customerId);
        List <Budget> budgets = budgetService.getCustomerBudgets(customerId);

        CustomerImportDTO customerImportDTO = customerService.transformEntityToDTO(c);
        List <ExpenseImportDTO> expenseImportDTOList = new ArrayList<>();
        for (Lead l : leads) {
            expenseImportDTOList.add(expenseService.transformEntityToDTO(l));
        }

        for (Ticket t : tickets) {
            expenseImportDTOList.add(expenseService.transformEntityToDTO(t));
        }

        List <BudgetImportDTO> budgetImportDTOList = new ArrayList<>();
        for (Budget b : budgets) {
            budgetImportDTOList.add(budgetService.transformEntityToDTO(b));
        }

        return new ExportDataDTO(customerImportDTO, expenseImportDTOList, budgetImportDTOList); 
    }

    @Override
    public String exportDataToCsv(int customerId) {
        ExportDataDTO data = exportDataToJson(customerId);
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add("key,value");

        CustomerImportDTO customer = data.getCustomerImportDTO();
        if (customer != null) {
            joiner.add(toCsvLine("customer.customer_email", customer.getCustomer_email()));
            joiner.add(toCsvLine("customer.customer_name", customer.getCustomer_name()));
            joiner.add(toCsvLine("customer.numLigne", customer.getNumLigne()));
        }

        List<ExpenseImportDTO> expenses = data.getExpenseImportDTOList();
        for (int i = 0; i < expenses.size(); i++) {
            ExpenseImportDTO expense = expenses.get(i);
            String prefix = "expenses[" + i + "]";
            joiner.add(toCsvLine(prefix + ".customer_email", expense.getCustomer_email()));
            joiner.add(toCsvLine(prefix + ".subject_or_name", expense.getSubject_or_name()));
            joiner.add(toCsvLine(prefix + ".type", expense.getType()));
            joiner.add(toCsvLine(prefix + ".status", expense.getStatus()));
            joiner.add(toCsvLine(prefix + ".expense", expense.getExpense()));
            joiner.add(toCsvLine(prefix + ".createdat", expense.getCreatedat()));
            joiner.add(toCsvLine(prefix + ".numLigne", expense.getNumLigne()));
        }

        List<BudgetImportDTO> budgets = data.getBudgetImportDTOList();
        for (int i = 0; i < budgets.size(); i++) {
            BudgetImportDTO budget = budgets.get(i);
            String prefix = "budgets[" + i + "]";
            joiner.add(toCsvLine(prefix + ".customer_email", budget.getCustomer_email()));
            joiner.add(toCsvLine(prefix + ".budget", budget.getBudget()));
            joiner.add(toCsvLine(prefix + ".numLigne", budget.getNumLigne()));
        }

        return joiner.toString();
    }

    private String toCsvLine(String key, Object value) {
        String valueAsText = value == null ? "" : String.valueOf(value);
        return escapeCsv(key) + "," + escapeCsv(valueAsText);
    }

    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        String escaped = value.replace("\"", "\"\"");
        if (escaped.contains(",") || escaped.contains("\n") || escaped.contains("\"")) {
            return "\"" + escaped + "\"";
        }
        return escaped;
    }

    
} 
