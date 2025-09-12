package com.gravityer.ecommerce.controller;

import com.gravityer.ecommerce.dto.MongoCustomerDto;
import com.gravityer.ecommerce.models.MongoCustomers;
import com.gravityer.ecommerce.services.MongoCustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Mongo-Customer Controller", description = "APIs for MongoDB Customer management")
public class MongoCustomerController {
    private final MongoCustomerService mongoCustomerService;

    // Getting all Customers from MongoDB collection (customers)
    @Operation(
            summary = "Get All Customers",
            description = "Fetches all customers from the MongoDB collection"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of customers"),
            @ApiResponse(responseCode = "404", description = "No customers found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/mongo/getAllCustomers")
    public ResponseEntity<BaseResponse<List<MongoCustomers>>> getAllCustomers() {
        var res = mongoCustomerService.getAllCustomers();
        if (res.isSuccess()) return ResponseEntity.ok(res);
        if (res.getMessage().contains("not found")) return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        return ResponseEntity.internalServerError().body(res);
    }

    // Getting a single Customer by ID from MongoDB collection (customers)
    @Operation(
            summary = "Get Customer by ID",
            description = "Fetches a customer by their ID from the MongoDB collection"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the customer"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/mongo/getCustomer/{customerId}")
    public ResponseEntity<BaseResponse<MongoCustomers>> getCustomerById(@PathVariable String customerId) {
        var res = mongoCustomerService.getCustomersById(customerId);
        if (res.isSuccess()) return ResponseEntity.ok(res);
        if (res.getMessage().contains("not found")) return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        return ResponseEntity.internalServerError().body(res);
    }

    // Adding a new Customer to MongoDB collection (customers)
    @Operation(
            summary = "Add New Customer",
            description = "Adds a new customer to the MongoDB collection"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer added successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/mongo/addCustomer")
    public ResponseEntity<BaseResponse<MongoCustomers>> addCustomer(@Valid @RequestBody MongoCustomerDto mongoCustomerDto) {
        var res = mongoCustomerService.addCustomer(mongoCustomerDto);
        if (res.isSuccess()) return ResponseEntity.ok(res);
        return ResponseEntity.internalServerError().body(res);
    }

    // Updating an existing Customer in MongoDB collection (customers)
    @Operation(
            summary = "Update Existing Customer",
            description = "Updates an existing customer in the MongoDB collection"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer updated successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "409", description = "Email already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/mongo/updateCustomer/{customerId}")
    public ResponseEntity<BaseResponse<MongoCustomers>> updateCustomer(@PathVariable String customerId, @RequestBody MongoCustomerDto mongoCustomerDto) {
        var res = mongoCustomerService.updateCustomer(customerId, mongoCustomerDto);
        if (res.isSuccess()) return ResponseEntity.ok(res);
        if (res.getMessage().contains("not found")) return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        if (res.getMessage().contains("already exists")) return new ResponseEntity<>(res, HttpStatus.CONFLICT);
        return ResponseEntity.internalServerError().body(res);
    }

    // Deleting a Customer from MongoDB collection (customers)
    @Operation(
            summary = "Delete Customer",
            description = "Deletes a customer from the MongoDB collection"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/mongo/deleteCustomer/{customerId}")
    public ResponseEntity<BaseResponse<String>> deleteCustomer(@PathVariable String customerId) {
        var res = mongoCustomerService.deleteCustomer(customerId);
        if (res.isSuccess()) return ResponseEntity.ok(res);
        if (res.getMessage().contains("not found")) return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        return ResponseEntity.internalServerError().body(res);
    }
}