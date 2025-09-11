package com.gravityer.ecommerce.services;

import com.gravityer.ecommerce.controller.BaseResponse;
import com.gravityer.ecommerce.dto.CustomerDto;
import com.gravityer.ecommerce.mapper.CustomerMapper;
import com.gravityer.ecommerce.models.Customer;
import com.gravityer.ecommerce.repositories.jpa.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    public BaseResponse<List<Customer>> getAllCustomers() {
        try {
            var allCustomers = customerRepository.findAll();
            if (allCustomers.isEmpty()) return new BaseResponse<>(true, "Customer List Empty", null);
            return new BaseResponse<>(true, "All Customer Fetched", allCustomers);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new BaseResponse<>(false, "Error getting all customers", null);
        }
    }

    public BaseResponse<Customer> getCustomerById(Long id) {
        try {
            Customer customer = customerRepository.findById(id).orElse(null);
            if (customer == null) return new BaseResponse<>(false, "Customer Not found", null);
            return new BaseResponse<>(true, "Customer Fetched", customer);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new BaseResponse<>(false, "Error getting customer", null);
        }
    }

    @Transactional
    public BaseResponse<Customer> addCustomer(CustomerDto customerDto) {
        try {
            var customer = customerMapper.toEntity(customerDto);
            customer.setCreatedAt(LocalDateTime.now());
            var newCustomer = customerRepository.save(customer);
            return new BaseResponse<>(true, "Customer Added Successfully", newCustomer);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new BaseResponse<>(false, "Error creating customer", null);
        }
    }

    @Transactional
    public BaseResponse<Customer> updateCustomer(Long customerId, CustomerDto customerDto) {
        try {
            Customer res =  customerRepository.findById(customerId).orElse(null);
            if (res == null) return new BaseResponse<>(false, "Customer Not Found", null);
            if (customerDto.getName()!=null) res.setName(customerDto.getName());
            if (customerDto.getEmail()!=null) res.setEmail(customerDto.getEmail());
            res.setUpdatedAt(LocalDateTime.now());
            customerRepository.saveAndFlush(res);
            return new BaseResponse<>(true, "Customer Updated Successfully", res);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new BaseResponse<>(false, "Error creating customer", null);
        }
    }

    @Transactional
    public BaseResponse<Customer> deleteCustomer(Long customerId) {
        try {
            Customer res =  customerRepository.findById(customerId).orElse(null);
            if (res == null) return new BaseResponse<>(false, "Customer Not Found", null);
            customerRepository.delete(res);
            return new BaseResponse<>(true, "Customer Deleted Successfully", res);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new BaseResponse<>(false, "Error deleting customer", null);
        }
    }
    
    // more than 3 orders
    public BaseResponse<List<Customer>> getCustomersWithMoreThanThreeOrders() {
        try {
            List<Customer> customers = customerRepository.findCustomersWithMoreThanThreeOrders();
            if (customers.isEmpty()) {
                return new BaseResponse<>(true, "No customers found with more than three orders", null);
            }
            return new BaseResponse<>(true, "Retrieved customers with more than three orders", customers);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new BaseResponse<>(false, "Error retrieving customers with more than three orders", null);
        }
    }
}
