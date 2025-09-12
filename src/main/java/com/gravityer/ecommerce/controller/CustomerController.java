package com.gravityer.ecommerce.controller;

import com.gravityer.ecommerce.dto.CustomerDto;
import com.gravityer.ecommerce.models.Customer;
import com.gravityer.ecommerce.services.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Customer Controller", description = "APIs for managing customers")
public class CustomerController {

    private final CustomerService customerService;

    @Operation(
            summary = "Get all customers",
            description = "Retrieve a list of all customers in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of customers"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/getAllCustomers")
    public ResponseEntity<BaseResponse<List<Customer>>> getAllCustomers() {
        var res = customerService.getAllCustomers();
        if (res.isSuccess()) return ResponseEntity.ok(res);
        return ResponseEntity.internalServerError().body(res);
    }


    @Operation(
            summary = "Get a customer by ID",
            description = "Retrieve a Customer by it's ID from the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of customers"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/getCustomer/{customerId}")
    public ResponseEntity<BaseResponse<Customer>> getCustomerById(@PathVariable Long customerId) {
        var res = customerService.getCustomerById(customerId);
        if (res.isSuccess()) return ResponseEntity.ok(res);
        if (res.getMessage().equals("Customer not found")) return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        return ResponseEntity.internalServerError().body(res);
    }


    @Operation(
            summary = "Add a new customer",
            description = "Add a new Customer to the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added a customer"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/addCustomer")
    public ResponseEntity<BaseResponse<Customer>> addCustomer(@RequestBody CustomerDto customerDto) {
        var res = customerService.addCustomer(customerDto);
        if (res.isSuccess()) return ResponseEntity.ok(res);
        return ResponseEntity.internalServerError().body(res);
    }


    @Operation(
            summary = "Update an existing customer",
            description = "Update the details of an existing Customer in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the customer"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/updateCustomer/{customer_id}")
    public ResponseEntity<BaseResponse<Customer>> updateCustomer(@PathVariable Long customer_id ,@RequestBody CustomerDto customerDto) {
        var res = customerService.updateCustomer(customer_id,customerDto);
        if (res.isSuccess()) return ResponseEntity.ok(res);
        if (res.getMessage().contains("not found")) return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        return ResponseEntity.internalServerError().body(res);
    }


    @Operation(
            summary = "Delete a customer",
            description = "Delete a Customer from the system by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the customer"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/deleteCustomer/{customer_id}")
    public ResponseEntity<BaseResponse<Customer>> deleteCustomer(@PathVariable Long customer_id) {
        var res = customerService.deleteCustomer(customer_id);
        if (res.isSuccess()) return ResponseEntity.ok(res);
        if (res.getMessage().contains("not found")) return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        return ResponseEntity.internalServerError().body(res);
    }


    @Operation(
            summary = "Get customers with more than three orders",
            description = "Retrieve a list of customers who have placed more than three orders"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of customers"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/getCustomersWithMoreThanThreeOrders")
    public ResponseEntity<BaseResponse<List<Customer>>> getCustomersWithMoreThanThreeOrders() {
        var res = customerService.getCustomersWithMoreThanThreeOrders();
        if (res.isSuccess()) return ResponseEntity.ok(res);
        return ResponseEntity.internalServerError().body(res);
    }

}
