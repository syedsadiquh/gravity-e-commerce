package com.gravityer.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerSpendDto {
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private double totalSpend;
}