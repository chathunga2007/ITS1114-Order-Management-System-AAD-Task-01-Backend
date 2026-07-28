package lk.ijse.OrderManagementSystem.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lk.ijse.OrderManagementSystem.dto.CustomerDTO;
import lk.ijse.OrderManagementSystem.entity.Customer;
import lk.ijse.OrderManagementSystem.exception.CustomException;
import lk.ijse.OrderManagementSystem.repository.CustomerRepository;
import lk.ijse.OrderManagementSystem.service.CustomerService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Execute method saveCustomer");
        if (customerDTO == null) {
            throw new CustomException(400, "Customer data cannot be null!");
        }
        if (customerDTO.getCustomerName() == null || customerDTO.getCustomerName().trim().isEmpty()) {
            throw new CustomException(400, "Customer name cannot be empty!");
        }
        if (customerDTO.getEmail() == null || customerDTO.getEmail().trim().isEmpty()) {
            throw new CustomException(400, "Customer email cannot be empty!");
        }
        if (customerDTO.getPhoneNumber() == null || customerDTO.getPhoneNumber().trim().isEmpty()) {
            throw new CustomException(400, "Customer phone number cannot be empty!");
        }

        Customer customer = new Customer();
        customer.setCustomerName(customerDTO.getCustomerName());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());

        Customer savedCustomer = customerRepository.save(customer);
        log.info("Customer Saved ...");

        CustomerDTO savedCustomerDTO = new CustomerDTO();
        savedCustomerDTO.setCustomerId(savedCustomer.getCustomerId());
        savedCustomerDTO.setCustomerName(savedCustomer.getCustomerName());
        savedCustomerDTO.setEmail(savedCustomer.getEmail());
        savedCustomerDTO.setPhoneNumber(savedCustomer.getPhoneNumber());
        log.info("Save Customer Returned ...");
        return savedCustomerDTO;
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        log.info("Execute method getAllCustomers");
        List<CustomerDTO> customerList = new ArrayList<>();
        List<Customer> customers = customerRepository.findAll();

        if (customers.isEmpty()) {
            throw new CustomException(404, "No customers found!");
        }

        for (Customer customer : customers) {
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setCustomerId(customer.getCustomerId());
            customerDTO.setCustomerName(customer.getCustomerName());
            customerDTO.setEmail(customer.getEmail());
            customerDTO.setPhoneNumber(customer.getPhoneNumber());

            customerList.add(customerDTO);
        }
        return customerList;
    }

    @Override
    public CustomerDTO getCustomerDetail(long id) {
        log.info("Execute method getCustomerDetail");
        if (id <= 0) {
            throw new CustomException(400, "Invalid customer ID: " + id);
        }
        Optional<Customer> customerOptional = customerRepository.findById(id);

        if (customerOptional.isEmpty()) {
            log.error("Customer with id {} does not exist", id);
            throw new CustomException(404, "Customer not found with id: " + id);
        }
        Customer customer = customerOptional.get();
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setCustomerId(customer.getCustomerId());
        customerDTO.setCustomerName(customer.getCustomerName());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());
        customerDTO.setEmail(customer.getEmail());
        return customerDTO;
    }

    @Override
    public void updateCustomer(CustomerDTO customerDTO) {
        log.info("Execute method updateCustomer");
        if (customerDTO == null || customerDTO.getCustomerId() <= 0) {
            throw new CustomException(400, "Invalid customer ID!");
        }
        if (customerDTO.getCustomerName() == null || customerDTO.getCustomerName().trim().isEmpty()) {
            throw new CustomException(400, "Customer name cannot be empty!");
        }
        if (customerDTO.getEmail() == null || customerDTO.getEmail().trim().isEmpty()) {
            throw new CustomException(400, "Customer email cannot be empty!");
        }
        if (customerDTO.getPhoneNumber() == null || customerDTO.getPhoneNumber().trim().isEmpty()) {
            throw new CustomException(400, "Customer phone number cannot be empty!");
        }

        Optional<Customer> customerOptional = customerRepository.findById(customerDTO.getCustomerId());
        if (customerOptional.isEmpty()) {
            log.error("Customer with id {} does not exist", customerDTO.getCustomerId());
            throw new CustomException(404, "Customer not found with id: " + customerDTO.getCustomerId());
        }
        Customer customer = customerOptional.get();
        customer.setCustomerName(customerDTO.getCustomerName());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
        customerRepository.save(customer);
    }

    @Override
    public void deleteCustomer(long customerId) {
        log.info("Execute method deleteCustomer");
        if (customerId <= 0) {
            throw new CustomException(400, "Invalid customer ID: " + customerId);
        }
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        if (customerOptional.isEmpty()) {
            log.error("Customer with id {} does not exist", customerId);
            throw new CustomException(404, "Customer not found with id: " + customerId);
        }
        customerRepository.deleteById(customerId);
        log.info("Customer deleted successfully with id: {}", customerId);
    }
}