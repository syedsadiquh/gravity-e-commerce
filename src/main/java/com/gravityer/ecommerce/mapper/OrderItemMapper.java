package com.gravityer.ecommerce.mapper;

import com.gravityer.ecommerce.dto.OrderItemDto;
import com.gravityer.ecommerce.models.OrderItem;
import com.gravityer.ecommerce.models.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(target = "product", source = "productId", qualifiedByName = "mapProduct")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orders", ignore = true)
    OrderItem toEntity(OrderItemDto dto);

    @Mapping(target = "productId", source = "product.id")
    OrderItemDto toDto(OrderItem entity);

    @Named("mapProduct")
    default Product mapProduct(Long productId) {
        if (productId == null) return null;
        Product p = new Product();
        p.setId(productId);
        return p;
    }
}
