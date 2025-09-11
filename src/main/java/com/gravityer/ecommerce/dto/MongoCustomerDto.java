package com.gravityer.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MongoCustomerDto {
    private String name;
    private String email;
    private String city;
}
