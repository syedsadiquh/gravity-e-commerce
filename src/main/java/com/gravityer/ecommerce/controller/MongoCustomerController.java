package com.gravityer.ecommerce.controller;

import com.gravityer.ecommerce.dto.MongoCustomerDto;
import com.gravityer.ecommerce.models.MongoCustomers;
import com.gravityer.ecommerce.services.MongoCustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MongoCustomerController {
    private final MongoCustomerService mongoCustomerService;

    @GetMapping("/mongo/getAllCustomers")
    public ResponseEntity<BaseResponse<List<MongoCustomers>>> getAllCustomers() {
        var res = mongoCustomerService.getAllCustomers();
        if (res.isSuccess()) return ResponseEntity.ok(res);
        return ResponseEntity.internalServerError().body(res);
    }

    @GetMapping("/mongo/getCustomer/{customerId}")
    public ResponseEntity<BaseResponse<MongoCustomers>> getCustomerById(@PathVariable String customerId) {
        var res = mongoCustomerService.getCustomersById(customerId);
        if (res.isSuccess()) return ResponseEntity.ok(res);
        return ResponseEntity.internalServerError().body(res);
    }

    @PostMapping("/mongo/addCustomer")
    public ResponseEntity<BaseResponse<MongoCustomers>> addCustomer(@Valid @RequestBody MongoCustomerDto mongoCustomerDto) {
        var res = mongoCustomerService.addCustomer(mongoCustomerDto);
        if (res.isSuccess()) return ResponseEntity.ok(res);
        return ResponseEntity.internalServerError().body(res);
    }

    @PutMapping("/mongo/updateCustomer/{customerId}")
    public ResponseEntity<BaseResponse<MongoCustomers>> updateCustomer(@PathVariable String customerId, @RequestBody MongoCustomerDto mongoCustomerDto) {
        var res = mongoCustomerService.updateCustomer(customerId, mongoCustomerDto);
        if (res.isSuccess()) return ResponseEntity.ok(res);
        return ResponseEntity.internalServerError().body(res);
    }

    @DeleteMapping("/mongo/deleteCustomer/{customerId}")
    public ResponseEntity<BaseResponse<String>> deleteCustomer(@PathVariable String customerId) {
        var res = mongoCustomerService.deleteCustomer(customerId);
        if (res.isSuccess()) return ResponseEntity.ok(res);
        return ResponseEntity.internalServerError().body(res);
    }
}