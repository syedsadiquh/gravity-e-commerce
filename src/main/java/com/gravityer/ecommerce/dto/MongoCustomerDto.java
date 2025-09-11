package com.gravityer.ecommerce.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MongoCustomerDto {
    @NotNull(message = "Name is required")
    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @NotNull(message = "Email is required")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @NotNull(message = "City is required")
    @NotEmpty(message = "City cannot be empty")
    private String city;
}
