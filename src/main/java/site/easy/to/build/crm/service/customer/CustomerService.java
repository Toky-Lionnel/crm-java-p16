package site.easy.to.build.crm.service.customer;

import site.easy.to.build.crm.dto.CustomerImportDTO;
import site.easy.to.build.crm.dto.ImportError;
import site.easy.to.build.crm.dto.ValidationResult;
import site.easy.to.build.crm.entity.Customer;

import java.util.List;

public interface CustomerService {

    public Customer findByCustomerId(int customerId);

    public List<Customer> findByUserId(int userId);

    public Customer findByEmail(String email);

    public List<Customer> findAll();

    public Customer save(Customer customer);

    public void delete(Customer customer);

    public List<Customer> getRecentCustomers(int userId, int limit);

    long countByUserId(int userId);

    double calculateTotalDepenses(int customerId);

    public List<ImportError> isDataValid (CustomerImportDTO customerImportDTO);

    public ValidationResult<CustomerImportDTO> validateCustomerImportData(List<CustomerImportDTO> customerImportDTOList);

    public Customer transformDTOtoEntity(CustomerImportDTO customerImportDTO);
}
