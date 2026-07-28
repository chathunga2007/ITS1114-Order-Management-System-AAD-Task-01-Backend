package lk.ijse.OrderManagementSystem.controller;

import lk.ijse.OrderManagementSystem.constant.CommonResponse;
import lk.ijse.OrderManagementSystem.dto.CustomerDTO;
import lk.ijse.OrderManagementSystem.service.CustomerService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static lk.ijse.OrderManagementSystem.constant.ResponseMessage.SUCCESS_MESSAGE;
import static lk.ijse.OrderManagementSystem.constant.ResponseStatusCode.OPERATION_SUCCESS;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveCustomer(@RequestBody CustomerDTO customerDTO) {
        customerService.saveCustomer(customerDTO);
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MESSAGE);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllCustomers() {
        List<CustomerDTO> allCustomers = customerService.getAllCustomers();
        return new CommonResponse(OPERATION_SUCCESS, allCustomers, SUCCESS_MESSAGE);
    }

    @GetMapping (value = "/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getCustomerDetail(@PathVariable long customerId) {
        CustomerDTO customerDetail = customerService.getCustomerDetail(customerId);
        return new CommonResponse(OPERATION_SUCCESS, customerDetail, SUCCESS_MESSAGE);
    }

    @PutMapping (produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateCustomer(@RequestBody CustomerDTO customerDTO) {
        customerService.updateCustomer(customerDTO);
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MESSAGE);
    }

    @DeleteMapping(value = "/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteCustomer(@PathVariable long customerId) {
        customerService.deleteCustomer(customerId);
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MESSAGE);
    }
}