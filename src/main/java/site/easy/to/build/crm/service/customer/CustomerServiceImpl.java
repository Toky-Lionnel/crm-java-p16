package site.easy.to.build.crm.service.customer;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.repository.CustomerRepository;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;
import site.easy.to.build.crm.dto.CustomerImportDTO;
import site.easy.to.build.crm.dto.ImportError;
import site.easy.to.build.crm.dto.ValidationResult;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.Ticket;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final TicketService ticketService;

    private final LeadService leadService;

    private final Validator validator;

    public CustomerServiceImpl(CustomerRepository customerRepository, TicketService ticketService, LeadService leadService,
                               Validator validator) {
        this.customerRepository = customerRepository;
        this.ticketService = ticketService;
        this.leadService = leadService;
        this.validator = validator;
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

        Customer customerToValidate = new Customer();
        customerToValidate.setEmail(customerImportDTO.getCustomer_email());
        customerToValidate.setName(customerImportDTO.getCustomer_name());
        customerToValidate.setCountry("Madagascar");

        Set<ConstraintViolation<Customer>> violations = validator.validate(customerToValidate);
        for (ConstraintViolation<Customer> violation : violations) {
            String field = violation.getPropertyPath().toString();
            errors.add(new ImportError("CUSTOMER", customerImportDTO.getNumLigne(), field + " : " + violation.getMessage()));
        }

        return errors;
    }

    
    @Override
    public ValidationResult<CustomerImportDTO> validateCustomerImportData(List<CustomerImportDTO> customerImportDTOList) {
        ValidationResult<CustomerImportDTO> validationResult = new ValidationResult<>();
        List<String> seenEmails = new ArrayList<>();
        
        for (CustomerImportDTO customerImportDTO : customerImportDTOList) {
            List<ImportError> errors = isDataValid(customerImportDTO);
            
            if (customerImportDTO.getCustomer_email() != null && !customerImportDTO.getCustomer_email().isEmpty()) {
                if (seenEmails.contains(customerImportDTO.getCustomer_email())) {
                    errors.add(new ImportError("CUSTOMER", customerImportDTO.getNumLigne(), "Email already exists in the import list : " + customerImportDTO.getCustomer_email()));
                } else {
                    seenEmails.add(customerImportDTO.getCustomer_email());
                }
            }
            
            if (errors.isEmpty()) {
                validationResult.getValidItems().add(customerImportDTO);
            } else {
                validationResult.getInvalidItems().add(customerImportDTO);
                validationResult.getErrors().addAll(errors);
            }
        }

        if (validationResult.getValidItems().isEmpty() && validationResult.getInvalidItems().isEmpty()) {
            return null;
        }

        validationResult.setTotalItems(customerImportDTOList.size());
        validationResult.setValidItemCount(validationResult.getValidItems().size());
        validationResult.setInvalidItemCount(validationResult.getInvalidItems().size());
        validationResult.setNomTable("CUSTOMER");
        

        
        return validationResult;
    }

    
    @Override
    public Customer transformDTOtoEntity(CustomerImportDTO customerImportDTO) {
        Customer customer = new Customer();
        customer.setName(customerImportDTO.getCustomer_name());
        customer.setEmail(customerImportDTO.getCustomer_email());
        customer.setCountry("Madagascar");
        customer.setCreatedAt(LocalDateTime.now());
        customer.setBudget(BigDecimal.ZERO);
        return customer;
    }


    @Override
    public List<ImportError> isCustomerValid (ValidationResult<CustomerImportDTO> validationResult, String customerEmail, String nomTable, int numLigne) {
        List<ImportError> errors = new ArrayList<>();

        if (validationResult == null) {
            Customer customer = findByEmail(customerEmail);
            if (customer == null) {
                errors.add(new ImportError(nomTable, numLigne, "Customer with email " + customerEmail + " not found in database"));
            }
            return errors;
        }

        List<CustomerImportDTO> validCustomers = validationResult.getValidItems();
        List<CustomerImportDTO> invalidCustomers = validationResult.getInvalidItems();

        boolean foundInValidCustomers = validCustomers.stream()
                .anyMatch(c -> Objects.equals(c.getCustomer_email(), customerEmail));
        boolean foundInInvalidCustomers = invalidCustomers.stream()
                .anyMatch(c -> Objects.equals(c.getCustomer_email(), customerEmail));

        if (foundInInvalidCustomers) {
            errors.add(new ImportError(nomTable, numLigne, "Customer with email " + customerEmail + " is invalid in customer import"));
        }

        if (!foundInValidCustomers && !foundInInvalidCustomers) {
            Customer customer = findByEmail(customerEmail);
            if (customer == null) {
                errors.add(new ImportError(nomTable, numLigne, "Customer with email " + customerEmail + " not found in customer import and database"));
            }
        }

        return errors;
    }

    
    @Override
    public CustomerImportDTO transformEntityToDTO (Customer c) {
        return new CustomerImportDTO("copy_" + c.getEmail(), c.getName(), c.getCustomerId());
    }

}
