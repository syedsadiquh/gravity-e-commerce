package com.gravityer.ecommerce.services;

import com.gravityer.ecommerce.controller.BaseResponse;
import com.gravityer.ecommerce.dto.MongoCustomerDto;
import com.gravityer.ecommerce.models.MongoCustomers;
import com.gravityer.ecommerce.repositories.mongo.MongoCustomerRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class MongoCustomerService {
    private final MongoCustomerRepository mongoCustomerRepository;

    public BaseResponse<List<MongoCustomers>> getAllCustomers() {
        try {
            var result = mongoCustomerRepository.findAll();
            if (result.isEmpty()) return new BaseResponse<>(true, "No customers found", result);
            return new BaseResponse<>(true, "Fetched all customers", result);
        } catch (Exception e) {
            return new BaseResponse<>(false, "Something went wrong: " + e.getMessage(), null);
        }
    }

    public BaseResponse<MongoCustomers> getCustomersById(String id) {
        try {
            var result = mongoCustomerRepository.findById(id).orElse(null);
            if (result == null) return new BaseResponse<>(true, "No customers found", null);
            return new BaseResponse<>(true, "Fetched customer with id: "+id, result);
        } catch (Exception e) {
            return new BaseResponse<>(false, "Something went wrong: " + e.getMessage(), null);
        }
    }

    @Transactional
    public BaseResponse<MongoCustomers> addCustomer(MongoCustomerDto customerDto) {
        try {
            var customer = new MongoCustomers();
            customer.setName(customerDto.getName());
            customer.setEmail(customerDto.getEmail());
            customer.setCity(customerDto.getCity());
            customer.setCreatedAt(LocalDateTime.now());
            var result = mongoCustomerRepository.save(customer);
            return new BaseResponse<>(true, "Customer added successfully", result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new BaseResponse<>(false, "Something went wrong: " + e.getMessage(), null);
        }
    }

    @Transactional
    public BaseResponse<MongoCustomers> updateCustomer(String customerId, MongoCustomerDto customerDto) {
        try {
            var existingCustomer = mongoCustomerRepository.findById(customerId).orElse(null);
            if (existingCustomer == null) return new BaseResponse<>(false, "Customer not found", null);
            existingCustomer.setName(customerDto.getName());
            existingCustomer.setEmail(customerDto.getEmail());
            existingCustomer.setCity(customerDto.getCity());
            existingCustomer.setUpdatedAt(LocalDateTime.now());
            var result = mongoCustomerRepository.save(existingCustomer);
            return new BaseResponse<>(true, "Customer updated successfully", result);
        } catch (DuplicateKeyException exception) {     // TODO: Can't reach this exception catch for some reason
            return new BaseResponse<>(false, "Email already exists", null);
        } catch (Exception e) {
            return new BaseResponse<>(false, "Something went wrong: " + e.getMessage(), null);
        }
    }

    @Transactional
    public BaseResponse<String> deleteCustomer(String customerId) {
        try {
            var existingCustomer = mongoCustomerRepository.findById(customerId).orElse(null);
            if (existingCustomer == null) return new BaseResponse<>(false, "Customer not found", null);
            mongoCustomerRepository.deleteById(customerId);
            return new BaseResponse<>(true, "Customer deleted successfully", "Deleted ID: " + customerId);
        } catch (Exception e) {
            return new BaseResponse<>(false, "Something went wrong: " + e.getMessage(), null);
        }
    }


}
