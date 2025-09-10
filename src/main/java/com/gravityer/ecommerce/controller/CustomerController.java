package com.gravityer.ecommerce.controller;

import com.gravityer.ecommerce.dto.CustomerDto;
import com.gravityer.ecommerce.models.Customer;
import com.gravityer.ecommerce.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/getAllCustomers")
    public ResponseEntity<BaseResponse<List<Customer>>> getAllCustomers() {
        var res = customerService.getAllCustomers();
        if (res.isSuccess()) return ResponseEntity.ok(res);
        return ResponseEntity.internalServerError().body(res);
    }

    @GetMapping("/getCustomer/{customerId}")
    public ResponseEntity<BaseResponse<Customer>> getCustomerById(@PathVariable Long customerId) {
        var res = customerService.getCustomerById(customerId);
        if (res.isSuccess()) return ResponseEntity.ok(res);
        return ResponseEntity.internalServerError().body(res);
    }

    @PostMapping("/addCustomer")
    public ResponseEntity<BaseResponse<Customer>> addCustomer(@RequestBody CustomerDto customerDto) {
        var res = customerService.addCustomer(customerDto);
        if (res.isSuccess()) return ResponseEntity.ok(res);
        return ResponseEntity.internalServerError().body(res);
    }

    @PutMapping("/updateCustomer/{customer_id}")
    public ResponseEntity<BaseResponse<Customer>> updateCustomer(@PathVariable Long customer_id ,@RequestBody CustomerDto customerDto) {
        var res = customerService.updateCustomer(customer_id,customerDto);
        if (res.isSuccess()) return ResponseEntity.ok(res);
        return ResponseEntity.internalServerError().body(res);
    }

    @DeleteMapping("/deleteCustomer/{customer_id}")
    public ResponseEntity<BaseResponse<Customer>> deleteCustomer(@PathVariable Long customer_id) {
        var res = customerService.deleteCustomer(customer_id);
        if (res.isSuccess()) return ResponseEntity.ok(res);
        return ResponseEntity.internalServerError().body(res);
    }


    @GetMapping("/getCustomersWithMoreThanThreeOrders")
    public ResponseEntity<BaseResponse<List<Customer>>> getCustomersWithMoreThanThreeOrders() {
        var res = customerService.getCustomersWithMoreThanThreeOrders();
        if (res.isSuccess()) return ResponseEntity.ok(res);
        return ResponseEntity.internalServerError().body(res);
    }

}
