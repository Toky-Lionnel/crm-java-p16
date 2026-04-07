package site.easy.to.build.crm.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import site.easy.to.build.crm.dto.TicketDto;
import site.easy.to.build.crm.dto.TicketRequestDto;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.ticket.TicketService;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.util.AuthenticationUtils;
import site.easy.to.build.crm.util.AuthorizationUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
public class TicketRestController {

    private final TicketService ticketService;
    private final UserService userService;
    private final CustomerService customerService;
    private final AuthenticationUtils authenticationUtils;

    public TicketRestController(TicketService ticketService, UserService userService,
                                CustomerService customerService, AuthenticationUtils authenticationUtils) {
        this.ticketService = ticketService;
        this.userService = userService;
        this.customerService = customerService;
        this.authenticationUtils = authenticationUtils;
    }

    @GetMapping
    public ResponseEntity<List<TicketDto>> getAllTickets(Authentication authentication) {
        User loggedInUser = getActiveLoggedInUser(authentication);

        List<Ticket> tickets;
        if (AuthorizationUtil.hasRole(authentication, "ROLE_MANAGER")) {
            tickets = ticketService.findAll();
        } else {
            tickets = ticketService.findEmployeeTickets(loggedInUser.getId());
        }

        List<TicketDto> response = tickets.stream().map(this::toDto).toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDto> getTicketById(@PathVariable("id") int id, Authentication authentication) {
        User loggedInUser = getActiveLoggedInUser(authentication);
        Ticket ticket = ticketService.findByTicketId(id);

        if (ticket == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found");
        }

        if (!AuthorizationUtil.hasRole(authentication, "ROLE_MANAGER")
                && !AuthorizationUtil.checkIfUserAuthorized(ticket.getEmployee(), loggedInUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        return ResponseEntity.ok(toDto(ticket));
    }

    @PostMapping
    public ResponseEntity<TicketDto> createTicket(@Valid @RequestBody TicketRequestDto request, Authentication authentication) {
        User loggedInUser = getActiveLoggedInUser(authentication);

        User employee = userService.findById(request.getEmployeeId());
        Customer customer = customerService.findByCustomerId(request.getCustomerId());

        if (employee == null || customer == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid employeeId or customerId");
        }

        if (AuthorizationUtil.hasRole(authentication, "ROLE_EMPLOYEE")) {
            if (!Objects.equals(employee.getId(), loggedInUser.getId())
                    || !Objects.equals(customer.getUser().getId(), loggedInUser.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
            }
        }

        Ticket ticket = new Ticket();
        applyRequestToTicket(ticket, request);
        ticket.setEmployee(employee);
        ticket.setCustomer(customer);
        ticket.setManager(loggedInUser);
        ticket.setCreatedAt(LocalDateTime.now());

        Ticket createdTicket = ticketService.save(ticket);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(createdTicket));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketDto> updateTicket(@PathVariable("id") int id,
                                                  @Valid @RequestBody TicketRequestDto request,
                                                  Authentication authentication) {
        User loggedInUser = getActiveLoggedInUser(authentication);

        Ticket ticket = ticketService.findByTicketId(id);
        if (ticket == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found");
        }

        if (!AuthorizationUtil.hasRole(authentication, "ROLE_MANAGER")
                && !AuthorizationUtil.checkIfUserAuthorized(ticket.getEmployee(), loggedInUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        User employee = userService.findById(request.getEmployeeId());
        Customer customer = customerService.findByCustomerId(request.getCustomerId());

        if (employee == null || customer == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid employeeId or customerId");
        }

        if (ticket.getManager().getId() == request.getEmployeeId()) {
            if (!AuthorizationUtil.hasRole(authentication, "ROLE_MANAGER")
                    && !Objects.equals(customer.getUser().getId(), loggedInUser.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
            }
        } else {
            if (!AuthorizationUtil.hasRole(authentication, "ROLE_MANAGER")
                    && !Objects.equals(ticket.getCustomer().getCustomerId(), request.getCustomerId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
            }
        }

        if (AuthorizationUtil.hasRole(authentication, "ROLE_EMPLOYEE")
                && !Objects.equals(employee.getId(), loggedInUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Employees can only assign tickets to themselves");
        }

        LocalDateTime createdAt = ticket.getCreatedAt();
        User manager = ticket.getManager();

        applyRequestToTicket(ticket, request);
        ticket.setEmployee(employee);
        ticket.setCustomer(customer);
        ticket.setManager(manager);
        ticket.setCreatedAt(createdAt);

        Ticket updatedTicket = ticketService.save(ticket);
        return ResponseEntity.ok(toDto(updatedTicket));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable("id") int id, Authentication authentication) {
        User loggedInUser = getActiveLoggedInUser(authentication);

        Ticket ticket = ticketService.findByTicketId(id);
        if (ticket == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found");
        }

        if (!AuthorizationUtil.hasRole(authentication, "ROLE_MANAGER")
                && !AuthorizationUtil.checkIfUserAuthorized(ticket.getEmployee(), loggedInUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        ticketService.delete(ticket);
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

    private void applyRequestToTicket(Ticket ticket, TicketRequestDto request) {
        ticket.setSubject(request.getSubject());
        ticket.setDescription(request.getDescription());
        ticket.setStatus(request.getStatus());
        ticket.setPriority(request.getPriority());
        ticket.setAmount(request.getAmount());
    }

    private TicketDto toDto(Ticket ticket) {
        TicketDto dto = new TicketDto();
        dto.setTicketId(ticket.getTicketId());
        dto.setSubject(ticket.getSubject());
        dto.setDescription(ticket.getDescription());
        dto.setStatus(ticket.getStatus());
        dto.setPriority(ticket.getPriority());
        dto.setAmount(ticket.getAmount());
        dto.setCreatedAt(ticket.getCreatedAt());

        if (ticket.getManager() != null) {
            dto.setManagerId(ticket.getManager().getId());
        }
        if (ticket.getEmployee() != null) {
            dto.setEmployeeId(ticket.getEmployee().getId());
        }
        if (ticket.getCustomer() != null) {
            dto.setCustomerId(ticket.getCustomer().getCustomerId());
        }

        return dto;
    }
}
