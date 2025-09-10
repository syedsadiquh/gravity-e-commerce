package com.gravityer.ecommerce.mapper;

import com.gravityer.ecommerce.dto.CustomerDto;
import com.gravityer.ecommerce.models.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper{

    CustomerDto toDto(Customer customer);

    @Mapping(target = "id", ignore = true)
    Customer toEntity(CustomerDto customerDto);
}
