package com.gravityer.ecommerce.services;

import com.gravityer.ecommerce.controller.BaseResponse;
import com.gravityer.ecommerce.dto.CustomerDto;
import com.gravityer.ecommerce.mapper.CustomerMapper;
import com.gravityer.ecommerce.models.Customer;
import com.gravityer.ecommerce.repositories.jpa.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<BaseResponse<List<Customer>>> getAllCustomers() {
        try {
            var allCustomers = customerRepository.findAll();
            if (allCustomers.isEmpty()) {
                return new ResponseEntity<>(new BaseResponse<>(true, "Customer List Empty", allCustomers), HttpStatus.OK);
            }
            return new ResponseEntity<>(new BaseResponse<>(true, "All Customer Fetched", allCustomers), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new BaseResponse<>(false, "Error getting all customers", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<BaseResponse<Customer>> getCustomerById(Long id) {
        try {
            Customer customer = customerRepository.findById(id).orElse(null);
            if (customer == null) {
                return new ResponseEntity<>(new BaseResponse<>(false, "Customer Not found", null), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(new BaseResponse<>(true, "Customer Fetched", customer), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new BaseResponse<>(false, "Error getting customer", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<BaseResponse<Customer>> addCustomer(CustomerDto customerDto) {
        try {
            var customer = customerMapper.toEntity(customerDto);
            customer.setCreatedAt(LocalDateTime.now());
            var newCustomer = customerRepository.save(customer);
            return new ResponseEntity<>(new BaseResponse<>(true, "Customer Added Successfully", newCustomer), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new BaseResponse<>(false, "Error creating customer", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<BaseResponse<Customer>> updateCustomer(Long customerId, CustomerDto customerDto) {
        try {
            Customer res =  customerRepository.findById(customerId).orElse(null);
            if (res == null) return new ResponseEntity<>(new BaseResponse<>(false, "Customer Not Found", null), HttpStatus.NOT_FOUND);
            if (customerDto.getName()!=null) res.setName(customerDto.getName());
            if (customerDto.getEmail()!=null) res.setEmail(customerDto.getEmail());
            res.setUpdatedAt(LocalDateTime.now());
            customerRepository.saveAndFlush(res);
            return new ResponseEntity<>(new BaseResponse<>(true, "Customer Updated Successfully", res), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new BaseResponse<>(false, "Error creating customer", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<BaseResponse<Customer>> deleteCustomer(Long customerId) {
        try {
            Customer res =  customerRepository.findById(customerId).orElse(null);
            if (res == null) return new ResponseEntity<>(new BaseResponse<>(false, "Customer Not Found", null), HttpStatus.NOT_FOUND);
            customerRepository.delete(res);
            return new ResponseEntity<>(new BaseResponse<>(true, "Customer Deleted Successfully", res), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new BaseResponse<>(false, "Error deleting customer", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // more than 3 orders
    public ResponseEntity<BaseResponse<List<Customer>>> getCustomersWithMoreThanThreeOrders() {
        try {
            List<Customer> customers = customerRepository.findCustomersWithMoreThanThreeOrders();
            if (customers.isEmpty()) {
                return new ResponseEntity<>(new BaseResponse<>(true, "No customers found with more than three orders", customers), HttpStatus.OK);
            }
            return new ResponseEntity<>(new BaseResponse<>(true, "Retrieved customers with more than three orders", customers), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new BaseResponse<>(false, "Error retrieving customers with more than three orders", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
