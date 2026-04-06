package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.*;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.util.*;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/budget")
public class BudgetController {

    private final BudgetService budgetService;

    private final AuthenticationUtils authenticationUtils;

    private final UserService userService;

    private final CustomerService customerService;

    @Autowired
    public BudgetController(AuthenticationUtils authenticationUtils, UserService userService,
                              CustomerService customerService, BudgetService budgetService) {
        this.budgetService = budgetService;
        this.authenticationUtils = authenticationUtils;
        this.userService = userService;
        this.customerService = customerService;
    }
    
    @GetMapping("/manager/show-all")
    public String getAllContracts(Model model) {
        List<Budget> budgets = budgetService.findAll();
        model.addAttribute("budgets", budgets);
        return "budget/budgets";
    }

    @GetMapping("/create")
    public String showCreatingForm(Model model, @RequestParam(value = "leadId", required = false) Integer leadId, Authentication authentication) {
        int userId = authenticationUtils.getLoggedInUserId(authentication);
        User user = userService.findById(userId);
        if (user.isInactiveUser()) {
            return "error/account-inactive";
        }

        List<Customer> customers;

        if (AuthorizationUtil.hasRole(authentication, "ROLE_MANAGER")) {
            customers = customerService.findAll();
        } else {
            customers = customerService.findByUserId(userId);
        }

        model.addAttribute("customers", customers);
        model.addAttribute("budget", new Budget());
        return "budget/create";
    }


    @PostMapping("/create")
    public String createNewContract(@ModelAttribute("budget") @Validated Budget budget, BindingResult bindingResult, @RequestParam("customerId") int customerId,
                                    Authentication authentication, Model model)
            throws IOException, GeneralSecurityException {

        int userId = authenticationUtils.getLoggedInUserId(authentication);
        User employee = userService.findById(userId);
        if (employee.isInactiveUser()) {
            return "error/account-inactive";
        }
        Customer customer = customerService.findByCustomerId(customerId);

        if (customer == null || customer.getUser().getId() != userId && !AuthorizationUtil.hasRole(authentication, "ROLE_MANAGER")) {
            return "error/500";
        }

        if (bindingResult.hasErrors()) {
            List<Customer> customers;

            if (AuthorizationUtil.hasRole(authentication, "ROLE_MANAGER")) {
                customers = customerService.findAll();
            } else {
                customers = customerService.findByUserId(userId);
            }

            model.addAttribute("customers", customers);
            model.addAttribute("budget", new Budget());
            return "budget/create";
        }
        budget.setCustomer(customer);
        budget.setCreatedAt(LocalDateTime.now());
        budgetService.save(budget);

        return "redirect:/budget/manager/show-all";
    }

}
