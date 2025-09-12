package com.gravityer.ecommerce.controller;

import com.gravityer.ecommerce.dto.OrderItemDto;
import com.gravityer.ecommerce.models.OrderItem;
import com.gravityer.ecommerce.services.OrderItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Order Item Controller", description = "APIs for managing order items")
public class OrderItemController {

    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }


    // Add OrderItem
    @Operation(
            summary = "Add a new order item",
            description = "Add a new Order Item to the system"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order Item added successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/addOrderItem")
    public ResponseEntity<BaseResponse<OrderItem>> addOrderItem(@Valid @RequestBody OrderItemDto orderItemDto) {
        return orderItemService.addOrderItem(orderItemDto);
    }

    // Get All OrderItems
    @Operation(
            summary = "Get all order items",
            description = "Retrieve a list of all order items in the system"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of order items"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/getAllOrderItems")
    public ResponseEntity<BaseResponse<List<OrderItem>>> getAllOrderItems() {
        return orderItemService.findAllOrderItems();
    }

    // Get OrderItems by ID
    @Operation(
            summary = "Get an order item by ID",
            description = "Retrieve an Order Item by its ID from the system"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the order item"),
            @ApiResponse(responseCode = "404", description = "Order Item not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/getOrderItemById/{orderItemId}")
    public ResponseEntity<BaseResponse<OrderItem>> getOrderItemById(@PathVariable long orderItemId) {
        return orderItemService.findOrderItemsByOrderId(orderItemId);
    }

    // Update Order Item
    @Operation(
            summary = "Update an existing order item",
            description = "Update the details of an existing Order Item in the system"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully updated the order item"),
            @ApiResponse(responseCode = "404", description = "Order Item not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/updateOrderItem/{orderItemId}")
    public ResponseEntity<BaseResponse<OrderItem>> updateProduct(@PathVariable long orderItemId, @Valid @RequestBody OrderItemDto orderItemDto) {
        return orderItemService.updateOrderItem(orderItemId, orderItemDto);
    }

    // Delete Order Item
    @Operation(
            summary = "Delete an order item",
            description = "Delete an Order Item from the system by its ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully deleted the order item"),
            @ApiResponse(responseCode = "404", description = "Order Item not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/deleteOrderItem/{orderItemId}")
    public ResponseEntity<BaseResponse<OrderItem>> deleteProduct(@PathVariable long orderItemId) {
        return orderItemService.deleteOrderItem(orderItemId);
    }

}
