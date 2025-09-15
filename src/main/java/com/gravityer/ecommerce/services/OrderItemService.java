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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;
    private final ProductRepository productRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public ResponseEntity<BaseResponse<List<OrderItem>>> findAllOrderItems() {
        try {
            if (redisTemplate.hasKey("OrderItemCache::allOrderItems")) {
                var cachedOrderItems = (List<OrderItem>) redisTemplate.opsForValue().get("OrderItemCache::allOrderItems");
                return new ResponseEntity<>(new BaseResponse<>(true, "OrderItem List from Cache", cachedOrderItems), HttpStatus.OK);
            }
            var orderItems = orderItemRepository.findAll();
            if (orderItems.isEmpty()) return new ResponseEntity<>(new BaseResponse<>(true, "OrderItem List is Empty", orderItems), HttpStatus.OK);
            redisTemplate.opsForValue().set("OrderItemCache::allOrderItems", orderItems, Duration.ofMinutes(5));
            return new ResponseEntity<>(new BaseResponse<>(true, "OrderItem List", orderItems), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new BaseResponse<>(false , "Internal Server Error", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<BaseResponse<OrderItem>> findOrderItemsByOrderId(Long orderItemId) {
        try {
            if (redisTemplate.opsForHash().hasKey("OrderItemCache::OrderItem", orderItemId)) {
                var cachedOrderItem = (OrderItem) redisTemplate.opsForHash().get("OrderItemCache::OrderItem", orderItemId);
                return new ResponseEntity<>(new BaseResponse<>(true, "Order Item from Cache", cachedOrderItem), HttpStatus.OK);
            }
            var orderItem = orderItemRepository.findById(orderItemId).orElseThrow(() -> new ItemNotFoundException("Order Item not found"));
            redisTemplate.opsForHash().put("OrderItemCache::OrderItem", orderItemId, orderItem);
            redisTemplate.opsForHash().expire("OrderItemCache::OrderItem", Duration.ofMinutes(5), Collections.singleton(String.valueOf(orderItemId)));

            return new ResponseEntity<>(new BaseResponse<>(true, "Order Items found", orderItem), HttpStatus.OK);
        } catch (ItemNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new BaseResponse<>(false , "Internal Server Error", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<BaseResponse<OrderItem>> addOrderItem(OrderItemDto orderItemDto) {
        try {
            var orderItem = orderItemMapper.toEntity(orderItemDto);

            Product product = productRepository.findById(orderItemDto.getProductId())
                    .orElseThrow(() -> new ItemNotFoundException("Product not found"));

            orderItem.setProduct(product);
            orderItem.setCreatedAt(LocalDateTime.now());
            orderItemRepository.save(orderItem);

            redisTemplate.opsForHash().put("OrderItemCache::OrderItem", orderItem.getId(), orderItem);
            redisTemplate.opsForHash().expire("OrderItemCache::OrderItem", Duration.ofMinutes(5), Collections.singleton(orderItem.getId().toString()));


            return new ResponseEntity<>(new BaseResponse<>(true, "Order Item Added Successfully", orderItem), HttpStatus.OK);
        } catch (ItemNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new BaseResponse<>(false , "Internal Server Error", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<BaseResponse<OrderItem>> updateOrderItem(Long orderItem_id, OrderItemDto orderItemDto) {
        try {
            var result = orderItemRepository.findById(orderItem_id).orElseThrow(
                    () -> new ItemNotFoundException("Order Item Not Found")
            );
            var product = productRepository.findById(orderItemDto.getProductId()).orElseThrow(
                    () -> new ItemNotFoundException("Product Not Found")
            );
            result.setProduct(product);
            result.setQuantity(orderItemDto.getQuantity());
            result.setUpdatedAt(LocalDateTime.now());
            orderItemRepository.save(result);
            if (redisTemplate.opsForHash().hasKey("OrderItemCache::OrderItem", orderItem_id)) {
                redisTemplate.opsForHash().delete("OrderItemCache::OrderItem", orderItem_id);
                redisTemplate.opsForHash().put("OrderItemCache::OrderItem",  orderItem_id, result);
                redisTemplate.opsForHash().expire("OrderItemCache::OrderItem", Duration.ofMinutes(5), Collections.singleton(String.valueOf(orderItem_id)));

            }
            return new ResponseEntity<>(new BaseResponse<>(true, "Order Item Updated Successfully", result), HttpStatus.OK);
        } catch (ItemNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new BaseResponse<>(false , "Internal Server Error", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<BaseResponse<OrderItem>> deleteOrderItem(Long orderItem_id) {
        try {
            var result = orderItemRepository.findById(orderItem_id).orElseThrow(() -> new ItemNotFoundException("Order Item not found"));
            orderItemRepository.deleteById(orderItem_id);
            if (redisTemplate.opsForHash().hasKey("OrderItemCache::OrderItem", orderItem_id)) {
                redisTemplate.opsForHash().delete("OrderItemCache::OrderItem", orderItem_id);
            }
            return new ResponseEntity<>(new BaseResponse<>(true, "Order Item Deleted Successfully", result), HttpStatus.OK);
        } catch (ItemNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(new BaseResponse<>(false , "Internal Server Error", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
