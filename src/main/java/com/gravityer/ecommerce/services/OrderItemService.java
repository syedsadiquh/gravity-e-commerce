package com.gravityer.ecommerce.services;

import com.gravityer.ecommerce.controller.BaseResponse;
import com.gravityer.ecommerce.dto.OrderItemDto;
import com.gravityer.ecommerce.exceptions.ItemNotFoundException;
import com.gravityer.ecommerce.mapper.OrderItemMapper;
import com.gravityer.ecommerce.models.OrderItem;
import com.gravityer.ecommerce.models.Product;
import com.gravityer.ecommerce.repositories.jpa.OrderItemRepository;
import com.gravityer.ecommerce.repositories.jpa.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;
    private final ProductRepository productRepository;

    public BaseResponse<List<OrderItem>> findAllOrderItems() {
        try {
            var orderItems = orderItemRepository.findAll();
            if (orderItems.isEmpty()) return new BaseResponse<>(true, "OrderItem List is Empty", orderItems);
            return new BaseResponse<>(true, "OrderItem List", orderItems);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new BaseResponse<>(false , "Internal Server Error", null);
        }

    }

    public BaseResponse<OrderItem> findOrderItemsByOrderId(Long orderItemId) {
        try {
            var orderItem = orderItemRepository.findById(orderItemId).orElseThrow(() -> new ItemNotFoundException("Order Item not found"));
            return new BaseResponse<>(true, "Order Items found", orderItem);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new BaseResponse<>(false , "Internal Server Error", null);
        }
    }

    @Transactional
    public BaseResponse<OrderItem> addOrderItem(OrderItemDto orderItemDto) {
        try {
            var orderItem = orderItemMapper.toEntity(orderItemDto);

            Product product = productRepository.findById(orderItemDto.getProductId())
                    .orElseThrow(() -> new ItemNotFoundException("Product not found"));

            orderItem.setProduct(product);
            orderItem.setCreatedAt(LocalDateTime.now());
            orderItemRepository.save(orderItem);
            return new BaseResponse<>(true, "Order Item Added Successfully", orderItem);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new BaseResponse<>(false , "Internal Server Error", null);
        }
    }

    @Transactional
    public BaseResponse<OrderItem> updateOrderItem(Long orderItem_id, OrderItemDto orderItemDto) {
        try {
            var result = orderItemRepository.findById(orderItem_id).orElse(null);
            if (result == null) return new BaseResponse<>(false, "Order Item Not Found", null);
            var product = productRepository.findById(orderItemDto.getProductId()).orElse(null);
            if (product == null) return new BaseResponse<>(false, "Product Not Found", null);
            result.setProduct(product);
            result.setQuantity(orderItemDto.getQuantity());
            result.setUpdatedAt(LocalDateTime.now());
            orderItemRepository.save(result);
            return new BaseResponse<>(true, "Order Item Updated Successfully", result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new BaseResponse<>(false , "Internal Server Error", null);
        }
    }

    @Transactional
    public BaseResponse<OrderItem> deleteOrderItem(Long orderItem_id) {
        try {
            var result = orderItemRepository.findById(orderItem_id).orElseThrow(() -> new ItemNotFoundException("Order Item not found"));
            orderItemRepository.deleteById(orderItem_id);
            return new BaseResponse<>(true, "Order Item Deleted Successfully", result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new BaseResponse<>(false , "Internal Server Error", null);
        }
    }
}
