package com.gravityer.ecommerce.controller;

import com.gravityer.ecommerce.dto.*;
import com.gravityer.ecommerce.models.OrderEntity;
import com.gravityer.ecommerce.services.OrderEntityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderEntityController {

    private final OrderEntityService orderEntityService;
    public OrderEntityController(OrderEntityService orderEntityService) {
        this.orderEntityService = orderEntityService;
    }

    // Add orderEntity
    @PostMapping("/addOrderEntity")
    public ResponseEntity<BaseResponse<OrderEntity>> addOrderEntity(@Valid @RequestBody AddOrderEntityDto addOrderEntityDto) {
        var response = orderEntityService.addOrderEntity(addOrderEntityDto);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Get All OrderEntity
    @GetMapping("/getAllOrderEntity")
    public ResponseEntity<BaseResponse<List<OrderEntity>>> getAllOrderEntity() {
        var response = orderEntityService.getAllOrderEntity();
        if (response.isSuccess()) return ResponseEntity.ok(response);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Get OrderEntity by ID
    @GetMapping("/getOrderEntityById/{orderEntityId}")
    public ResponseEntity<BaseResponse<OrderEntity>> getOrderEntityById(@PathVariable long orderEntityId) {
        var response = orderEntityService.getOrderEntityById(orderEntityId);
        if (response.isSuccess()) return ResponseEntity.ok(response);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Update Order Entity
    @PutMapping("/updateOrderEntity/{orderEntityId}")
    public ResponseEntity<BaseResponse<OrderEntity>> updateOrderEntity(@PathVariable long orderEntityId, @RequestBody AddOrderEntityDto addOrderEntityDto) {
        var response = orderEntityService.updateOrderEntity(orderEntityId, addOrderEntityDto);
        if (response.isSuccess()) return ResponseEntity.ok(response);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Delete Order Entity
    @DeleteMapping("/deleteOrderEntity/{orderEntityId}")
    public ResponseEntity<BaseResponse<OrderEntity>>deleteOrderEntity(@PathVariable long orderEntityId) {
        var response = orderEntityService.deleteOrderEntity(orderEntityId);
        if (response.isSuccess()) return ResponseEntity.ok(response);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Getting Order with all customer and product details
    @GetMapping("/getOrdersWithDetails")
    public ResponseEntity<BaseResponse<List<OrderDetailDto>>> getOrdersWithCustomerAndProductDetails() {
        var response = orderEntityService.getOrdersWithCustomerAndProductDetails();
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    // total spend per customer
    @GetMapping("/getTotalSpendPerCustomer")
    public ResponseEntity<BaseResponse<List<CustomerSpendDto>>> getTotalSpendPerCustomer() {
        var response = orderEntityService.getTotalSpendPerCustomer();
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    // total sales per product
    @GetMapping("/getTotalSalesPerProduct")
    public ResponseEntity<BaseResponse<List<ProductSalesDto>>> getTotalSalesPerProduct() {
        var response = orderEntityService.getTotalSalesPerProduct();
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
