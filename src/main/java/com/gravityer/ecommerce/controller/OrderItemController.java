package com.gravityer.ecommerce.controller;

import com.gravityer.ecommerce.dto.OrderItemDto;
import com.gravityer.ecommerce.models.OrderItem;
import com.gravityer.ecommerce.services.OrderItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderItemController {

    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }


    // Add OrderItem
    @PostMapping("/addOrderItem")
    public ResponseEntity<BaseResponse<OrderItem>> addOrderItem(@Valid @RequestBody OrderItemDto orderItemDto) {
        var response = orderItemService.addOrderItem(orderItemDto);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Get All OrderItems
    @GetMapping("/getAllOrderItems")
    public ResponseEntity<BaseResponse<List<OrderItem>>> getAllOrderItems() {
        var response = orderItemService.findAllOrderItems();
        if (response.isSuccess()) return ResponseEntity.ok(response);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Get OrderItems by ID
    @GetMapping("/getOrderItemById/{orderItemId}")
    public ResponseEntity<BaseResponse<OrderItem>> getOrderItemById(@PathVariable long orderItemId) {
        var response = orderItemService.findOrderItemsByOrderId(orderItemId);
        if (response.isSuccess()) return ResponseEntity.ok(response);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Update Order Item
    @PutMapping("/updateOrderItem/{orderItemId}")
    public ResponseEntity<BaseResponse<OrderItem>> updateProduct(@PathVariable long orderItemId, @RequestBody OrderItemDto orderItemDto) {
        var response = orderItemService.updateOrderItem(orderItemId, orderItemDto);
        if (response.isSuccess()) return ResponseEntity.ok(response);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Delete Order Item
    @DeleteMapping("/deleteOrderItem/{orderItemId}")
    public ResponseEntity<BaseResponse<OrderItem>> deleteProduct(@PathVariable long orderItemId) {
        var response = orderItemService.deleteOrderItem(orderItemId);
        if (response.isSuccess()) return ResponseEntity.ok(response);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
