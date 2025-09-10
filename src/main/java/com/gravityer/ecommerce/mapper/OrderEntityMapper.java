package com.gravityer.ecommerce.mapper;

import com.gravityer.ecommerce.dto.OrderEntityDto;
import com.gravityer.ecommerce.models.Customer;
import com.gravityer.ecommerce.models.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderEntityMapper {

    @Mapping(target = "customer", source = "customer_id", qualifiedByName = "mapCustomer")
    @Mapping(target = "id", ignore = true) // auto-generated
    OrderEntity toEntity(OrderEntityDto dto);

    @Mapping(target = "customer_id", source = "customer.id")
    OrderEntityDto toDto(OrderEntity entity);

    @Named("mapCustomer")
    default Customer mapCustomer(Long customerId) {
        if (customerId == null) return null;
        Customer c = new Customer();
        c.setId(customerId);
        return c;
    }
}

