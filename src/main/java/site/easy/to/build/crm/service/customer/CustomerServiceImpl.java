package site.easy.to.build.crm.service.customer;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.repository.CustomerRepository;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;
import site.easy.to.build.crm.dto.CustomerImportDTO;
import site.easy.to.build.crm.dto.ImportError;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.Ticket;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final TicketService ticketService;

    private final LeadService leadService;

    public CustomerServiceImpl(CustomerRepository customerRepository, TicketService ticketService, LeadService leadService) {
        this.customerRepository = customerRepository;
        this.ticketService = ticketService;
        this.leadService = leadService;
    }

    @Override
    public Customer findByCustomerId(int customerId) {
        return customerRepository.findByCustomerId(customerId);
    }

    @Override
    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public List<Customer> findByUserId(int userId) {
        return customerRepository.findByUserId(userId);
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public void delete(Customer customer) {
        customerRepository.delete(customer);
    }

    @Override
    public List<Customer> getRecentCustomers(int userId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return customerRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    @Override
    public long countByUserId(int userId) {
        return customerRepository.countByUserId(userId);
    }

    @Override
    public double calculateTotalDepenses(int customerId) {
        List<Ticket> tickets = ticketService.findCustomerTickets(customerId);
        List<Lead> leads = leadService.getCustomerLeads(customerId);

        double totalExpenses = 0;

        for (Ticket ticket : tickets) {
            totalExpenses += ticket.getAmount().doubleValue();
        }

        for (Lead lead : leads) {
            totalExpenses += lead.getAmount().doubleValue();
        }

        return totalExpenses;
    }

    @Override
    public List<ImportError> isDataValid(CustomerImportDTO customerImportDTO){
        List<ImportError> errors = new ArrayList<>();

        Customer existingCustomer = customerRepository.findByEmail(customerImportDTO.getCustomer_email());
        if (existingCustomer != null) {
            errors.add(new ImportError("CUSTOMER", customerImportDTO.getNumLigne(), "Email already exists for customer"));
        }

        if (customerImportDTO.getCustomer_email() == null || customerImportDTO.getCustomer_email().isEmpty()) {
            errors.add(new ImportError("CUSTOMER", customerImportDTO.getNumLigne(), "Email is required for customer"));
        }
        if (customerImportDTO.getCustomer_name() == null || customerImportDTO.getCustomer_name().isEmpty()) {
            errors.add(new ImportError("CUSTOMER", customerImportDTO.getNumLigne(), "Name is required for customer"));
        }
        return errors;
    }

}
