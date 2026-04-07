package site.easy.to.build.crm.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import site.easy.to.build.crm.dto.LeadDto;
import site.easy.to.build.crm.dto.LeadRequestDto;
import site.easy.to.build.crm.dto.UpdateAmountRequest;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.util.AuthenticationUtils;
import site.easy.to.build.crm.util.AuthorizationUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/leads")
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
public class LeadRestController {

    private final LeadService leadService;
    private final UserService userService;
    private final CustomerService customerService;
    private final AuthenticationUtils authenticationUtils;

    public LeadRestController(LeadService leadService, UserService userService,
                              CustomerService customerService, AuthenticationUtils authenticationUtils) {
        this.leadService = leadService;
        this.userService = userService;
        this.customerService = customerService;
        this.authenticationUtils = authenticationUtils;
    }

    @GetMapping
    public ResponseEntity<List<LeadDto>> getAllLeads(Authentication authentication) {
        User loggedInUser = getActiveLoggedInUser(authentication);

        List<Lead> leads;
        if (AuthorizationUtil.hasRole(authentication, "ROLE_MANAGER")) {
            leads = leadService.findAll();
        } else {
            Map<Integer, Lead> uniqueLeads = new LinkedHashMap<>();
            for (Lead lead : leadService.findAssignedLeads(loggedInUser.getId())) {
                uniqueLeads.put(lead.getLeadId(), lead);
            }
            for (Lead lead : leadService.findCreatedLeads(loggedInUser.getId())) {
                uniqueLeads.put(lead.getLeadId(), lead);
            }
            leads = new ArrayList<>(uniqueLeads.values());
        }

        List<LeadDto> response = leads.stream().map(this::toDto).toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeadDto> getLeadById(@PathVariable("id") int id, Authentication authentication) {
        User loggedInUser = getActiveLoggedInUser(authentication);
        Lead lead = leadService.findByLeadId(id);

        if (lead == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lead not found");
        }

        if (!AuthorizationUtil.hasRole(authentication, "ROLE_MANAGER")
                && !AuthorizationUtil.checkIfUserAuthorized(lead.getEmployee(), loggedInUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        return ResponseEntity.ok(toDto(lead));
    }

    @PostMapping
    public ResponseEntity<LeadDto> createLead(@Valid @RequestBody LeadRequestDto request, Authentication authentication) {
        User loggedInUser = getActiveLoggedInUser(authentication);

        User employee = userService.findById(request.getEmployeeId());
        Customer customer = customerService.findByCustomerId(request.getCustomerId());

        if (employee == null || customer == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid employeeId or customerId");
        }

        if (AuthorizationUtil.hasRole(authentication, "ROLE_EMPLOYEE") && !Objects.equals(employee.getId(), loggedInUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Employees can only assign leads to themselves");
        }

        Lead lead = new Lead();
        applyRequestToLead(lead, request);
        lead.setEmployee(employee);
        lead.setCustomer(customer);
        lead.setManager(loggedInUser);
        lead.setCreatedAt(LocalDateTime.now());

        Lead createdLead = leadService.save(lead);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(createdLead));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeadDto> updateLead(@PathVariable("id") int id,
                                              @Valid @RequestBody LeadRequestDto request,
                                              Authentication authentication) {
        User loggedInUser = getActiveLoggedInUser(authentication);

        Lead lead = leadService.findByLeadId(id);
        if (lead == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lead not found");
        }

        if (!AuthorizationUtil.hasRole(authentication, "ROLE_MANAGER")
                && !AuthorizationUtil.checkIfUserAuthorized(lead.getEmployee(), loggedInUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        User employee = userService.findById(request.getEmployeeId());
        Customer customer = customerService.findByCustomerId(request.getCustomerId());

        if (employee == null || customer == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid employeeId or customerId");
        }

        if (AuthorizationUtil.hasRole(authentication, "ROLE_EMPLOYEE") && !Objects.equals(employee.getId(), loggedInUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Employees can only assign leads to themselves");
        }

        LocalDateTime createdAt = lead.getCreatedAt();
        User manager = lead.getManager();

        applyRequestToLead(lead, request);
        lead.setEmployee(employee);
        lead.setCustomer(customer);
        lead.setManager(manager);
        lead.setCreatedAt(createdAt);

        Lead updatedLead = leadService.save(lead);
        return ResponseEntity.ok(toDto(updatedLead));
    }



    @PutMapping("/{id}/amount")
    public ResponseEntity<LeadDto> updateLeadAmount(@PathVariable("id") int id,
                                                    @Valid @RequestBody UpdateAmountRequest request,
                                                    Authentication authentication) {
        User loggedInUser = getActiveLoggedInUser(authentication);

        Lead lead = leadService.findByLeadId(id);
        if (lead == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lead not found");
        }

        if (!AuthorizationUtil.hasRole(authentication, "ROLE_MANAGER")
                && !AuthorizationUtil.checkIfUserAuthorized(lead.getEmployee(), loggedInUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
        lead.setAmount(BigDecimal.valueOf(request.getAmount()));
        Lead updatedLead = leadService.save(lead);
        return ResponseEntity.ok(toDto(updatedLead));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLead(@PathVariable("id") int id, Authentication authentication) {
        User loggedInUser = getActiveLoggedInUser(authentication);

        Lead lead = leadService.findByLeadId(id);
        if (lead == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lead not found");
        }

        if (!AuthorizationUtil.hasRole(authentication, "ROLE_MANAGER")
                && !AuthorizationUtil.checkIfUserAuthorized(lead.getEmployee(), loggedInUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        leadService.delete(lead);
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

    private void applyRequestToLead(Lead lead, LeadRequestDto request) {
        lead.setName(request.getName());
        lead.setStatus(request.getStatus());
        lead.setPhone(request.getPhone());
        lead.setMeetingId(request.getMeetingId());
        lead.setGoogleDrive(request.getGoogleDrive());
        lead.setGoogleDriveFolderId(request.getGoogleDriveFolderId());
        lead.setAmount(request.getAmount());
    }

    private LeadDto toDto(Lead lead) {
        LeadDto dto = new LeadDto();
        dto.setLeadId(lead.getLeadId());
        dto.setName(lead.getName());
        dto.setStatus(lead.getStatus());
        dto.setPhone(lead.getPhone());
        dto.setMeetingId(lead.getMeetingId());
        dto.setGoogleDrive(lead.getGoogleDrive());
        dto.setGoogleDriveFolderId(lead.getGoogleDriveFolderId());
        dto.setAmount(lead.getAmount());
        dto.setCreatedAt(lead.getCreatedAt());

        if (lead.getManager() != null) {
            dto.setManagerId(lead.getManager().getId());
        }
        if (lead.getEmployee() != null) {
            dto.setEmployeeId(lead.getEmployee().getId());
        }
        if (lead.getCustomer() != null) {
            dto.setCustomerId(lead.getCustomer().getCustomerId());
        }

        return dto;
    }
}
