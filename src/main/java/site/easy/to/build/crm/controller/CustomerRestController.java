package site.easy.to.build.crm.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import site.easy.to.build.crm.dto.BudgetCreateRequestDto;
import site.easy.to.build.crm.dto.BudgetResponseDto;
import site.easy.to.build.crm.dto.CustomerDto;
import site.easy.to.build.crm.dto.CustomerRequestDto;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.CustomerLoginInfo;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.contract.ContractService;
import site.easy.to.build.crm.service.customer.CustomerLoginInfoService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.imports.ImportService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.util.AuthenticationUtils;
import site.easy.to.build.crm.util.AuthorizationUtil;
import site.easy.to.build.crm.util.EmailTokenUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
public class CustomerRestController {

    private final CustomerService customerService;
    private final UserService userService;
    private final CustomerLoginInfoService customerLoginInfoService;
    private final AuthenticationUtils authenticationUtils;
    private final TicketService ticketService;
    private final ContractService contractService;
    private final LeadService leadService;
    private final BudgetService budgetService;
    private final ImportService importService;

    public CustomerRestController(CustomerService customerService, UserService userService,
                                  CustomerLoginInfoService customerLoginInfoService,
                                  AuthenticationUtils authenticationUtils,
                                  TicketService ticketService,
                                  ContractService contractService,
                                  LeadService leadService,
                                  BudgetService budgetService,
                                  ImportService importService) {
        this.customerService = customerService;
        this.userService = userService;
        this.customerLoginInfoService = customerLoginInfoService;
        this.authenticationUtils = authenticationUtils;
        this.ticketService = ticketService;
        this.contractService = contractService;
        this.leadService = leadService;
        this.budgetService = budgetService;
        this.importService = importService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getCustomers(Authentication authentication) {
        User loggedInUser = getActiveLoggedInUser(authentication);

        List<Customer> customers;
        if (AuthorizationUtil.hasRole(authentication, "ROLE_MANAGER")) {
            customers = customerService.findAll();
        } else {
            customers = customerService.findByUserId(loggedInUser.getId());
        }

        return ResponseEntity.ok(customers.stream().map(this::toDto).toList());
    }

    @PostMapping ("/import")
    public ResponseEntity<String> importData(@RequestBody String json) throws Exception {
        return ResponseEntity.ok(this.importService.processImport(this.importService.parseJson(json)).toString());
    }



    @GetMapping("/budgets")
    public ResponseEntity<List<BudgetResponseDto>> getCustomersBudgets(Authentication authentication) {
        User loggedInUser = getActiveLoggedInUser(authentication);

        List<Budget> budgets;
        if (AuthorizationUtil.hasRole(authentication, "ROLE_MANAGER")) {
            budgets = budgetService.findAll();
        } else {
            budgets = budgetService.findAll()
                    .stream()
                    .filter(budget -> budget.getCustomer() != null
                            && budget.getCustomer().getUser() != null
                            && Objects.equals(budget.getCustomer().getUser().getId(), loggedInUser.getId()))
                    .toList();
        }

        List<BudgetResponseDto> response = budgets.stream().map(this::toBudgetDto).toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/budgets")
    public ResponseEntity<BudgetResponseDto> createBudget(@Valid @RequestBody BudgetCreateRequestDto request,
                                                          Authentication authentication) {
        User loggedInUser = getActiveLoggedInUser(authentication);

        Customer customer = customerService.findByCustomerId(request.getCustomerId());
        if (customer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
        }

        boolean isManager = AuthorizationUtil.hasRole(authentication, "ROLE_MANAGER");
        boolean isOwner = customer.getUser() != null && Objects.equals(customer.getUser().getId(), loggedInUser.getId());

        if (!isManager && !isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        Budget budget = new Budget();
        budget.setAmount(request.getAmount());
        budget.setCustomer(customer);
        budget.setCreatedAt(LocalDateTime.now());

        Budget createdBudget = budgetService.save(budget);
        return ResponseEntity.status(HttpStatus.CREATED).body(toBudgetDto(createdBudget));
    }

    @GetMapping("/{id}/budgets")
    public ResponseEntity<List<BudgetResponseDto>> getCustomerBudgetsByCustomerId(@PathVariable("id") int id,
                                                                                  Authentication authentication) {
        User loggedInUser = getActiveLoggedInUser(authentication);

        Customer customer = customerService.findByCustomerId(id);
        if (customer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
        }

        boolean isManager = AuthorizationUtil.hasRole(authentication, "ROLE_MANAGER");
        boolean isOwner = customer.getUser() != null && Objects.equals(customer.getUser().getId(), loggedInUser.getId());
        if (!isManager && !isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        List<BudgetResponseDto> response = budgetService.getCustomerBudgets(id)
                .stream()
                .map(this::toBudgetDto)
                .toList();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable("id") int id, Authentication authentication) {
        User loggedInUser = getActiveLoggedInUser(authentication);
        Customer customer = customerService.findByCustomerId(id);

        if (customer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
        }

        boolean isOwner = AuthorizationUtil.checkIfUserAuthorized(customer.getUser(), loggedInUser);
        if (!AuthorizationUtil.hasRole(authentication, "ROLE_MANAGER") && !isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        return ResponseEntity.ok(toDto(customer));
    }

    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@Valid @RequestBody CustomerRequestDto request,
                                                      Authentication authentication) {
        User loggedInUser = getActiveLoggedInUser(authentication);

        Customer existingByEmail = customerService.findByEmail(request.getEmail());
        if (existingByEmail != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already used by another customer");
        }

        User owner = resolveOwnerForCreate(authentication, loggedInUser, request.getUserId());

        Customer customer = new Customer();
        applyRequestToCustomer(customer, request);
        customer.setUser(owner);
        customer.setCreatedAt(LocalDateTime.now());

        CustomerLoginInfo customerLoginInfo = new CustomerLoginInfo();
        customerLoginInfo.setEmail(request.getEmail());
        customerLoginInfo.setToken(EmailTokenUtils.generateToken());
        customerLoginInfo.setPasswordSet(false);

        CustomerLoginInfo savedLoginInfo = customerLoginInfoService.save(customerLoginInfo);
        customer.setCustomerLoginInfo(savedLoginInfo);

        Customer createdCustomer = customerService.save(customer);
        savedLoginInfo.setCustomer(createdCustomer);

        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(createdCustomer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable("id") int id,
                                                      @Valid @RequestBody CustomerRequestDto request,
                                                      Authentication authentication) {
        User loggedInUser = getActiveLoggedInUser(authentication);
        Customer customer = customerService.findByCustomerId(id);

        if (customer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
        }

        boolean isManager = AuthorizationUtil.hasRole(authentication, "ROLE_MANAGER");
        boolean isOwner = AuthorizationUtil.checkIfUserAuthorized(customer.getUser(), loggedInUser);
        if (!isManager && !isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        Customer existingByEmail = customerService.findByEmail(request.getEmail());
        if (existingByEmail != null && !Objects.equals(existingByEmail.getCustomerId(), customer.getCustomerId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already used by another customer");
        }

        User owner = resolveOwnerForUpdate(authentication, loggedInUser, request.getUserId(), customer.getUser());

        LocalDateTime createdAt = customer.getCreatedAt();

        applyRequestToCustomer(customer, request);
        customer.setUser(owner);
        customer.setCreatedAt(createdAt);

        CustomerLoginInfo loginInfo = customer.getCustomerLoginInfo();
        if (loginInfo != null) {
            loginInfo.setEmail(request.getEmail());
            customerLoginInfoService.save(loginInfo);
        }

        Customer updatedCustomer = customerService.save(customer);
        return ResponseEntity.ok(toDto(updatedCustomer));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteCustomer(@PathVariable("id") int id, Authentication authentication) {
        getActiveLoggedInUser(authentication);

        if (!AuthorizationUtil.hasRole(authentication, "ROLE_MANAGER")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only managers can delete customers");
        }

        Customer customer = customerService.findByCustomerId(id);
        if (customer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
        }

        CustomerLoginInfo customerLoginInfo = customer.getCustomerLoginInfo();

        contractService.deleteAllByCustomer(customer);
        leadService.deleteAllByCustomer(customer);
        ticketService.deleteAllByCustomer(customer);

        if (customerLoginInfo != null) {
            customerLoginInfoService.delete(customerLoginInfo);
        }
        customerService.delete(customer);

        return ResponseEntity.noContent().build();
    }

    private User getActiveLoggedInUser(Authentication authentication) {
        int userId = authenticationUtils.getLoggedInUserId(authentication);
        User user = userService.findById(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }
        if (user.isInactiveUser()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Inactive account");
        }
        return user;
    }

    private User resolveOwnerForCreate(Authentication authentication, User loggedInUser, Integer requestedUserId) {
        if (AuthorizationUtil.hasRole(authentication, "ROLE_MANAGER") && requestedUserId != null) {
            User requestedOwner = userService.findById(requestedUserId);
            if (requestedOwner == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid userId");
            }
            return requestedOwner;
        }
        return loggedInUser;
    }

    private User resolveOwnerForUpdate(Authentication authentication, User loggedInUser,
                                       Integer requestedUserId, User currentOwner) {
        if (AuthorizationUtil.hasRole(authentication, "ROLE_MANAGER") && requestedUserId != null) {
            User requestedOwner = userService.findById(requestedUserId);
            if (requestedOwner == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid userId");
            }
            return requestedOwner;
        }
        if (AuthorizationUtil.hasRole(authentication, "ROLE_MANAGER")) {
            return currentOwner;
        }
        return loggedInUser;
    }

    private void applyRequestToCustomer(Customer customer, CustomerRequestDto request) {
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPosition(request.getPosition());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        customer.setCity(request.getCity());
        customer.setState(request.getState());
        customer.setCountry(request.getCountry());
        customer.setDescription(request.getDescription());
        customer.setTwitter(request.getTwitter());
        customer.setFacebook(request.getFacebook());
        customer.setYoutube(request.getYoutube());
        customer.setBudget(request.getBudget());
    }

    private CustomerDto toDto(Customer customer) {
        CustomerDto dto = new CustomerDto();
        dto.setCustomerId(customer.getCustomerId());
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail());
        dto.setPosition(customer.getPosition());
        dto.setPhone(customer.getPhone());
        dto.setAddress(customer.getAddress());
        dto.setCity(customer.getCity());
        dto.setState(customer.getState());
        dto.setCountry(customer.getCountry());
        dto.setDescription(customer.getDescription());
        dto.setTwitter(customer.getTwitter());
        dto.setFacebook(customer.getFacebook());
        dto.setYoutube(customer.getYoutube());
        dto.setBudget(customer.getBudget());
        dto.setCreatedAt(customer.getCreatedAt());

        if (customer.getUser() != null) {
            dto.setUserId(customer.getUser().getId());
        }

        return dto;
    }

    private BudgetResponseDto toBudgetDto(Budget budget) {
        BudgetResponseDto dto = new BudgetResponseDto();
        dto.setId(budget.getId());
        dto.setAmount(budget.getAmount());
        dto.setCreatedAt(budget.getCreatedAt());

        if (budget.getCustomer() != null) {
            dto.setCustomerName(budget.getCustomer().getName());
            dto.setCustomerId(budget.getCustomer().getCustomerId());
        }

        return dto;
    }
}
