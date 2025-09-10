package com.gravityer.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailDto {
    private Long orderId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate orderDate;
    
    private Long customerId;
    private String customerName;
    private String customerEmail;
    
    private List<OrderItemDetailDto> items;
    
    // Nested for showing out...
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OrderItemDetailDto {
        private Long orderItemId;
        private int quantity;
        
        // Product details
        private Long productId;
        private String productName;
        private double productPrice;
        
        // Calculated field
        private double subtotal;
    }
}