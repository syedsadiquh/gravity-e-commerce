package com.gravityer.ecommerce.services;

import com.gravityer.ecommerce.controller.BaseResponse;
import com.gravityer.ecommerce.dto.ProductDto;
import com.gravityer.ecommerce.mapper.ProductMapper;
import com.gravityer.ecommerce.models.Product;
import com.gravityer.ecommerce.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public BaseResponse<List<Product>> getProducts() {
        return new BaseResponse<>(true, "All Products", productRepository.findAll());
    }

    public BaseResponse<Product> getProductById(long productId) {
        try {
            var product = productRepository.findById(productId).orElse(null);
            if (product == null) {
                return new BaseResponse<>(false , "Product not found", null);
            }
            return new BaseResponse<>(true, "Product with id: " + productId, product);
        } catch (Exception e) {
            return new BaseResponse<>(false, "Internal Server Error", null);
        }
    }

    @Transactional
    public BaseResponse<Product> addProduct(ProductDto productDto) {
        try{
            Product product = productMapper.toEntity(productDto);
            product.setCreatedAt(LocalDateTime.now());
            productRepository.saveAndFlush(product);
            return new BaseResponse<>(true, "Product Created", product);
        } catch(Exception e){
            log.error(e.getMessage());
            return new BaseResponse<>(false, "Error adding product: " + e.getMessage(), null);
        }
    }

    @Transactional
    public BaseResponse<Product> updateProduct(long productId, ProductDto productDto) {
        try {
            Product product = productRepository.findById(productId).orElse(null);
            if (product == null) return new BaseResponse<>(false, "Product not found", null);
            product.setName(productDto.getName());
            product.setPrice(productDto.getPrice());
            product.setUpdatedAt(LocalDateTime.now());
            var res = productRepository.saveAndFlush(product);
            return new BaseResponse<>(true, "Product Updated", res);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new BaseResponse<>(false, "Error updating product: " + e.getMessage(), null);
        }
    }

    @Transactional
    public BaseResponse<Product> deleteProduct(long productId) {
        try {
            var res = productRepository.deleteProductById(productId);
            if (res == 1) return new BaseResponse<>(true, "Product Deleted Successfully", null);
            return new BaseResponse<>(false, "Product not found", null);
        }  catch (Exception e) {
            log.error(e.getMessage());
            return new BaseResponse<>(false, "Error deleting product: " + e.getMessage(), null);
        }
    }

    public BaseResponse<Page<Product>> listProducts(PageRequest pageable) {
        try {
            var res = productRepository.findAll(pageable);
            if (res.isEmpty()) return new BaseResponse<>(true, "No Products Exists", null);
            return new BaseResponse<>(true, "All Products", res);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new BaseResponse<>(false, "Internal Server Error", null);
        }
    }
}
