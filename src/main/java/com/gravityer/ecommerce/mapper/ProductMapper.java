package com.gravityer.ecommerce.mapper;

import com.gravityer.ecommerce.dto.ProductDto;
import com.gravityer.ecommerce.models.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDto toDto(Product product);

    Product toEntity(ProductDto productDto);
}
