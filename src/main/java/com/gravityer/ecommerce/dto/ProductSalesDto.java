package com.gravityer.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductSalesDto {
    private Long productId;
    private String productName;
    private double productPrice;
    private Long totalQuantitySold;
    private Double totalRevenue;
}