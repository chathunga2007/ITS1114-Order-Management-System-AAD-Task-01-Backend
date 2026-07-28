package lk.ijse.OrderManagementSystem.service;

import lk.ijse.OrderManagementSystem.dto.CustomerDTO;

import java.util.List;

public interface CustomerService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    List<CustomerDTO> getAllCustomers();
    CustomerDTO getCustomerDetail(long id);
    void updateCustomer(CustomerDTO customerDTO);
    void deleteCustomer(long customerId);
}