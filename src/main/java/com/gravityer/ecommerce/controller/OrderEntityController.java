package com.gravityer.ecommerce.controller;

import com.gravityer.ecommerce.dto.*;
import com.gravityer.ecommerce.models.OrderEntity;
import com.gravityer.ecommerce.services.OrderEntityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Order Entity Controller", description = "APIs for managing orders")
public class OrderEntityController {

    private final OrderEntityService orderEntityService;
    public OrderEntityController(OrderEntityService orderEntityService) {
        this.orderEntityService = orderEntityService;
    }

    // Add orderEntity
    @Operation(
            summary = "Add a new order",
            description = "Add a new Order to the system"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "OrderEntity created successfully"),
            @ApiResponse(responseCode = "404", description = "Related Customer or Product not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/addOrderEntity")
    public ResponseEntity<BaseResponse<OrderEntity>> addOrderEntity(@Valid @RequestBody AddOrderEntityDto addOrderEntityDto) {
        return orderEntityService.addOrderEntity(addOrderEntityDto);
    }

    // Get All OrderEntity
    @Operation(
            summary = "Get all Orders",
            description = "Retrieve a list of all Orders in the system"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of Orders"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/getAllOrderEntity")
    public ResponseEntity<BaseResponse<List<OrderEntity>>> getAllOrderEntity() {
        return orderEntityService.getAllOrderEntity();
    }

    // Get OrderEntity by ID
    @Operation(
            summary = "Get an Order by ID",
            description = "Retrieve an Order by its ID from the system"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the Order"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/getOrderEntityById/{orderEntityId}")
    public ResponseEntity<BaseResponse<OrderEntity>> getOrderEntityById(@PathVariable long orderEntityId) {
        return orderEntityService.getOrderEntityById(orderEntityId);
    }

    // Update Order Entity
    @Operation(
            summary = "Update an existing Order",
            description = "Update the details of an existing Order in the system"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully updated the Order"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/updateOrderEntity/{orderEntityId}")
    public ResponseEntity<BaseResponse<OrderEntity>> updateOrderEntity(@PathVariable long orderEntityId, @Valid @RequestBody AddOrderEntityDto addOrderEntityDto) {
        return orderEntityService.updateOrderEntity(orderEntityId, addOrderEntityDto);
    }

    // Delete Order Entity
    @Operation(
            summary = "Delete an Order",
            description = "Delete an Order from the system by its ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully deleted the Order"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/deleteOrderEntity/{orderEntityId}")
    public ResponseEntity<BaseResponse<OrderEntity>>deleteOrderEntity(@PathVariable long orderEntityId) {
        return orderEntityService.deleteOrderEntity(orderEntityId);
    }

    // Getting Order with all customer and product details
    @Operation(
            summary = "Get Orders with Customer and Product Details",
            description = "Retrieve a list of All Orders along with their associated Customer and Product details"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of Orders with details"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/getOrdersWithDetails")
    public ResponseEntity<BaseResponse<List<OrderDetailDto>>> getOrdersWithCustomerAndProductDetails() {
        return orderEntityService.getOrdersWithCustomerAndProductDetails();
    }
    
    // total spend per customer
    @Operation(
            summary = "Get Total Spend Per Customer",
            description = "Retrieve the total amount spent by each customer"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved total spend per customer"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/getTotalSpendPerCustomer")
    public ResponseEntity<BaseResponse<List<CustomerSpendDto>>> getTotalSpendPerCustomer() {
        return orderEntityService.getTotalSpendPerCustomer();
    }
    
    // total sales per product
    @Operation(
            summary = "Get Total Sales Per Product",
            description = "Retrieve the total sales amount for each product"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved total sales per product"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/getTotalSalesPerProduct")
    public ResponseEntity<BaseResponse<List<ProductSalesDto>>> getTotalSalesPerProduct() {
        return orderEntityService.getTotalSalesPerProduct();
    }

    // get orders by customer id
    @Operation(
            summary = "Get Orders by Customer ID",
            description = "Retrieve a list of Orders placed by a specific Customer using their ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of Orders for the customer"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/getOrdersByCustomerId/{customerId}")
    public ResponseEntity<BaseResponse<List<OrderEntity>>> getOrdersByCustomerId(@PathVariable Long customerId) {
        return orderEntityService.getOrdersByCustomerId(customerId);
    }

    /*
    To remember for later:
    REQUEST EXAMPLE:
        {
          "customer_id": 152,
          "order_date": "10-09-2025",
          "orderItemDtos": [
            {"productId": 1, "quantity": 2},
            {"productId": 2, "quantity": 2},
            {"productId": 3, "quantity": 3}
          ]
        }

     */

    @Operation(
            summary = "Place a new order",
            description = "Create a new order with associated order items for a customer"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successfully placed the order"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Customer or Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/placeOrder")
    public ResponseEntity<BaseResponse<OrderEntity>> placeOrder(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Details of the order to be placed",
                    required = true,
                    content = @Content(schema = @Schema(implementation = PlaceOrderDto.class, example = """
                            {
                                      "customer_id": 152,
                                      "order_date": "10-09-2025",
                                      "orderItemDtos": [
                                        {"productId": 1, "quantity": 2},
                                        {"productId": 2, "quantity": 2},
                                        {"productId": 3, "quantity": 3}
                                      ]
                                    }
                            """))
            )
            @RequestBody PlaceOrderDto placeOrderDto
    ) {
        return orderEntityService.placeOrder(placeOrderDto);
    }
}
