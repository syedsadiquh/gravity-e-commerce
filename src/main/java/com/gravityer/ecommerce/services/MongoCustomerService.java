package com.gravityer.ecommerce.services;

import com.gravityer.ecommerce.controller.BaseResponse;
import com.gravityer.ecommerce.dto.MongoCustomerDto;
import com.gravityer.ecommerce.exceptions.ItemNotFoundException;
import com.gravityer.ecommerce.models.MongoCustomers;
import com.gravityer.ecommerce.repositories.mongo.MongoCustomerRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class MongoCustomerService {
    private final MongoCustomerRepository mongoCustomerRepository;

    public ResponseEntity<BaseResponse<List<MongoCustomers>>> getAllCustomers() {
        try {
            var result = mongoCustomerRepository.findAll();
            if (result.isEmpty()) {
                return new ResponseEntity<>(new BaseResponse<>(true, "Customer List Empty", result), HttpStatus.OK);
            }
            return new ResponseEntity<>(new BaseResponse<>(true, "Fetched all customers", result), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new BaseResponse<>(false, "Something went wrong: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<BaseResponse<MongoCustomers>> getCustomersById(String id) {
        try {
            var result = mongoCustomerRepository.findById(new ObjectId(id)).orElseThrow(
                    () -> new ItemNotFoundException("Customer with id " + id + " not found")
            );
            return new ResponseEntity<>(new BaseResponse<>(true, "Fetched customer with id: "+id, result), HttpStatus.OK);
        } catch (ItemNotFoundException e) {
            throw e;
        } catch (Exception e) {
            return new ResponseEntity<>(new BaseResponse<>(false, "Something went wrong: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<BaseResponse<MongoCustomers>> addCustomer(MongoCustomerDto customerDto) {
        try {
            var customer = new MongoCustomers();
            customer.setName(customerDto.getName());
            customer.setEmail(customerDto.getEmail());
            customer.setCity(customerDto.getCity());
            customer.setCreatedAt(LocalDateTime.now());
            var result = mongoCustomerRepository.save(customer);
            return new ResponseEntity<>(new BaseResponse<>(true, "Customer added successfully", result), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new BaseResponse<>(false, "Something went wrong: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<BaseResponse<MongoCustomers>> updateCustomer(String customerId, MongoCustomerDto customerDto) {
        try {
            var existingCustomer = mongoCustomerRepository.findById(new ObjectId(customerId)).orElseThrow(
                    () -> new ItemNotFoundException("Customer with id " + customerId + " not found")
            );
            existingCustomer.setName(customerDto.getName());
            existingCustomer.setEmail(customerDto.getEmail());
            existingCustomer.setCity(customerDto.getCity());
            existingCustomer.setUpdatedAt(LocalDateTime.now());
            var result = mongoCustomerRepository.save(existingCustomer);
            return new ResponseEntity<>(new BaseResponse<>(true, "Customer updated successfully", result), HttpStatus.OK);
        } catch (ItemNotFoundException e) {
            throw e;
        } catch (DuplicateKeyException exception) {     // TODO: Can't reach this exception catch for some reason
            return new ResponseEntity<>(new BaseResponse<>(false, "Email already exists", null), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(new BaseResponse<>(false, "Something went wrong: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<BaseResponse<String>> deleteCustomer(String customerId) {
        try {
            var existingCustomer = mongoCustomerRepository.findById(new ObjectId(customerId)).orElseThrow(
                    () -> new ItemNotFoundException("Customer with id " + customerId + " not found")
            );
            mongoCustomerRepository.deleteById(new ObjectId(customerId));
            return new ResponseEntity<>(new BaseResponse<>(true, "Customer deleted successfully", "Deleted ID: " + customerId), HttpStatus.OK);
        } catch (ItemNotFoundException e) {
            throw e;
        } catch (Exception e) {
            return new ResponseEntity<>(new BaseResponse<>(false, "Something went wrong: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
