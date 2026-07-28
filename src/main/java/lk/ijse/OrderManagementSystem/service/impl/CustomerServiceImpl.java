package lk.ijse.OrderManagementSystem.service.impl;

import lk.ijse.OrderManagementSystem.dto.CustomerDTO;
import lk.ijse.OrderManagementSystem.entity.Customer;
import lk.ijse.OrderManagementSystem.repository.CustomerRepository;
import lk.ijse.OrderManagementSystem.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Execute method saveCustomer");
        try {
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
        } catch (Exception e) {
            log.error("Error occurred while saving customer: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        log.info("Execute method getAllCustomers");
        try {

            List<CustomerDTO> customerList = new ArrayList<>();
            List<Customer> customers = customerRepository.findAll();

            for (Customer customer : customers) {
                CustomerDTO customerDTO = new CustomerDTO();
                customerDTO.setCustomerId(customer.getCustomerId());
                customerDTO.setCustomerName(customer.getCustomerName());
                customerDTO.setEmail(customer.getEmail());
                customerDTO.setPhoneNumber(customer.getPhoneNumber());

                customerList.add(customerDTO);
            }
            return customerList;

        } catch (Exception e) {
            log.error("Error occurred while retrieving customers: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public CustomerDTO getCustomerDetail(long id) {
        log.info("Execute method getCustomerDetail");
        try {

            Optional<Customer> customerOptional = customerRepository.findById(id);

            if (!customerOptional.isPresent()) {
                log.error("Customer with id {} does not exist", id);
                throw new RuntimeException("Customer not found with id: " + id);
            }
            Customer  customer = customerOptional.get();
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setCustomerId(customer.getCustomerId());
            customerDTO.setCustomerName(customer.getCustomerName());
            customerDTO.setPhoneNumber(customer.getPhoneNumber());
            customerDTO.setEmail(customer.getEmail());
            return customerDTO;

        } catch (Exception e) {
            log.error("Error occurred while retrieving customer detail: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void updateCustomer(CustomerDTO customerDTO) {
        log.info("Execute method updateCustomer");
        try {

            Optional<Customer> customerOptional = customerRepository.findById(customerDTO.getCustomerId());
            if (!customerOptional.isPresent()) {
                log.error("Customer with id {} does not exist", customerDTO.getCustomerId());
                throw new RuntimeException("Customer not found with id: " + customerDTO.getCustomerId());
            }
            Customer customer = customerOptional.get();
            customer.setCustomerName(customerDTO.getCustomerName());
            customer.setEmail(customerDTO.getEmail());
            customer.setPhoneNumber(customerDTO.getPhoneNumber());
            customerRepository.save(customer);

        } catch (Exception e) {
            log.error("Error occurred while updating customer: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteCustomer(long customerId) {
        log.info("Execute method deleteCustomer");
        try {
            Optional<Customer> customerOptional = customerRepository.findById(customerId);
            if (!customerOptional.isPresent()) {
                log.error("Customer with id {} does not exist", customerId);
                throw new RuntimeException("Customer not found with id: " + customerId);
            }
            customerRepository.deleteById(customerId);
            log.info("Customer deleted successfully with id: {}", customerId);
        } catch (Exception e) {
            log.error("Error occurred while deleting customer: {}", e.getMessage());
            throw e;
        }
    }
}