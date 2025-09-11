package com.gravityer.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvgRatingCustomerDto {
    private String customerId;
    private String customerName;
    private String customerEmail;
    private String customerCity;
    private Double averageRating;
}
