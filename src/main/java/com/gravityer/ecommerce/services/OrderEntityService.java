package com.gravityer.ecommerce.services;

import com.gravityer.ecommerce.controller.BaseResponse;
import com.gravityer.ecommerce.dto.*;
import com.gravityer.ecommerce.exceptions.ItemNotFoundException;
import com.gravityer.ecommerce.mapper.OrderEntityMapper;
import com.gravityer.ecommerce.mapper.OrderItemMapper;
import com.gravityer.ecommerce.models.Customer;
import com.gravityer.ecommerce.models.OrderEntity;
import com.gravityer.ecommerce.models.OrderItem;
import com.gravityer.ecommerce.repositories.CustomerRepository;
import com.gravityer.ecommerce.repositories.OrderEntityRepository;
import com.gravityer.ecommerce.repositories.OrderItemRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OrderEntityService {
    @Autowired
    private OrderEntityRepository orderEntityRepository;
    @Autowired
    private OrderEntityMapper orderEntityMapper;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private OrderItemRepository orderItemRepository;

    public BaseResponse<List<OrderEntity>> getAllOrderEntity() {
        try {
            List<OrderEntity> orderEntities = orderEntityRepository.findAll();
            if (orderEntities.isEmpty()) return new BaseResponse<>(true, "No Order Entities Found", null);
            return new BaseResponse<>(true, "All Order Entity found", orderEntities);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new BaseResponse<>(false , "Internal Server Error", null);
        }
    }

    public BaseResponse<OrderEntity> getOrderEntityById(Long id) {
        try {
            OrderEntity orderEntity = orderEntityRepository.findById(id).orElse(null);
            if (orderEntity == null) return new BaseResponse<>(true, "Order Entity not Found", null);
            return new BaseResponse<>(true, "Order Entity found", orderEntity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new BaseResponse<>(false , "Internal Server Error", null);
        }
    }

    @Transactional
    public BaseResponse<OrderEntity> addOrderEntity(AddOrderEntityDto addOrderEntityDto) {
        Customer customer = customerRepository.findById(addOrderEntityDto.getCustomer_id())
                .orElseThrow(() -> new ItemNotFoundException("Customer not found with id: " + addOrderEntityDto.getCustomer_id()));

        List<OrderItem> items = addOrderEntityDto.getOrderItemIds().stream()
                .map(addEntityId -> orderItemRepository.findById(addEntityId)
                        .orElseThrow(() -> new ItemNotFoundException("Order Item not found with id: " + addEntityId)))
                .toList();

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setCustomer(customer);
        orderEntity.setOrder_date(addOrderEntityDto.getOrder_date());

        OrderEntity finalOrderEntity = orderEntity;
        items.forEach(item -> item.setOrders(finalOrderEntity));
        orderEntity.setItems(items);

        try {
            orderEntity = orderEntityRepository.save(orderEntity);
            return new BaseResponse<>(true, "Order Entity Added Successfully", orderEntityRepository.findById(orderEntity.getId()).orElse(null));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new BaseResponse<>(false , "Internal Server Error", null);
        }
    }

    @Transactional
    public BaseResponse<OrderEntity> updateOrderEntity(Long id, AddOrderEntityDto addOrderEntityDto) {

        OrderEntity orderEntity = orderEntityRepository.findById(id).orElse(null);
        if (orderEntity == null) return new BaseResponse<>(false, "Order Entity not Found", null);

        Customer customer = customerRepository.findById(addOrderEntityDto.getCustomer_id())
                .orElseThrow(() -> new ItemNotFoundException("Customer not found with id: " + addOrderEntityDto.getCustomer_id()));
        orderEntity.setCustomer(customer);
        orderEntity.setOrder_date(addOrderEntityDto.getOrder_date());

        List<OrderItem> items = addOrderEntityDto.getOrderItemIds().stream()
                .map(addEntityId -> orderItemRepository.findById(addEntityId)
                        .orElseThrow(() -> new ItemNotFoundException("Order Item not found with id: " + addEntityId)))
                .toList();

        orderEntity.getItems().clear(); // delete and readd new ones...
        for (OrderItem item : items) {
            item.setOrders(orderEntity);
            orderEntity.getItems().add(item);
        }

        try {

            OrderEntity newOrderEntity = orderEntityRepository.save(orderEntity);
            return new BaseResponse<>(true, "Order Entity Updated Successfully", newOrderEntity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new BaseResponse<>(false , "Internal Server Error", null);
        }
    }

    @Transactional
    public BaseResponse<OrderEntity> deleteOrderEntity(Long id) {
        try {
            OrderEntity orderEntity = orderEntityRepository.findById(id).orElse(null);
            if (orderEntity == null) return new BaseResponse<>(false, "Order Entity not Found", null);
            orderEntityRepository.delete(orderEntity);
            return new BaseResponse<>(true, "Order Entity Deleted Successfully", orderEntity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new BaseResponse<>(false , "Internal Server Error", null);
        }
    }
    
    // Order Details DTO for returning details out
    public BaseResponse<List<OrderDetailDto>> getOrdersWithCustomerAndProductDetails() {
        try {
            List<OrderEntity> orders = orderEntityRepository.findAllOrdersWithCustomerAndProducts();
            
            if (orders.isEmpty()) return new BaseResponse<>(true, "No orders found", new ArrayList<>());
            
            // mapper for Order entity to order details
            List<OrderDetailDto> orderDetails = orders.stream()
                .map(order -> {
                    Customer customer = order.getCustomer();

                    OrderDetailDto detailDto = OrderDetailDto.builder()
                        .orderId(order.getId())
                        .orderDate(order.getOrder_date())
                        .customerId(customer.getId())
                        .customerName(customer.getName())
                        .customerEmail(customer.getEmail())
                        .items(new ArrayList<>())
                        .build();
                    
                    if (order.getItems() != null) {
                        List<OrderDetailDto.OrderItemDetailDto> itemDtos = order.getItems().stream()
                            .map(item -> {
                                double subtotal = item.getQuantity() * item.getProduct().getPrice();
                                
                                return OrderDetailDto.OrderItemDetailDto.builder()
                                    .orderItemId(item.getId())
                                    .quantity(item.getQuantity())
                                    .productId(item.getProduct().getId())
                                    .productName(item.getProduct().getName())
                                    .productPrice(item.getProduct().getPrice())
                                    .subtotal(subtotal)
                                    .build();
                            })
                                .toList();
                        
                        detailDto.setItems(itemDtos);
                    }
                    
                    return detailDto;
                })
                .toList();
            
            return new BaseResponse<>(true, "Orders with details retrieved successfully", orderDetails);
        } catch (Exception e) {
            log.error("Error retrieving orders with details: {}", e.getMessage(), e);
            return new BaseResponse<>(false, "Error retrieving orders with details", null);
        }
    }
    
    // total spend
    public BaseResponse<List<CustomerSpendDto>> getTotalSpendPerCustomer() {
        try {
            List<CustomerSpendDto> customerSpends = orderEntityRepository.getTotalSpendPerCustomer();
            
            if (customerSpends.isEmpty()) {
                return new BaseResponse<>(true, "No customer spending data found", new ArrayList<>());
            }
            
            return new BaseResponse<>(true, "Total spend per customer retrieved successfully", customerSpends);
        } catch (Exception e) {
            log.error("Error retrieving total spend per customer: {}", e.getMessage(), e);
            return new BaseResponse<>(false, "Error retrieving total spend per customer", null);
        }
    }
    
    // total sales for products
    public BaseResponse<List<ProductSalesDto>> getTotalSalesPerProduct() {
        try {
            List<ProductSalesDto> productSales = orderEntityRepository.getTotalSalesPerProduct();
            
            if (productSales.isEmpty()) {
                return new BaseResponse<>(true, "No product sales data found", new ArrayList<>());
            }
            
            return new BaseResponse<>(true, "Total sales per product retrieved successfully", productSales);
        } catch (Exception e) {
            log.error("Error retrieving total sales per product: {}", e.getMessage(), e);
            return new BaseResponse<>(false, "Error retrieving total sales per product", null);
        }
    }
}
