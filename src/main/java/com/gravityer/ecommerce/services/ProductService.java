package com.gravityer.ecommerce.services;

import com.gravityer.ecommerce.controller.BaseResponse;
import com.gravityer.ecommerce.dto.ProductDto;
import com.gravityer.ecommerce.mapper.ProductMapper;
import com.gravityer.ecommerce.models.Product;
import com.gravityer.ecommerce.repositories.jpa.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ProductHelper productHelper;

    public ResponseEntity<BaseResponse<List<Product>>> getProducts() {
        try {
            var result = productHelper.getProductsImp();
            if (result.isEmpty()) {
                return new ResponseEntity<>(new BaseResponse<>(true, "Product List Empty", result), HttpStatus.OK);
            }
            return new ResponseEntity<>(new BaseResponse<>(true, "All Products", productRepository.findAll()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new BaseResponse<>(false, "Unable to get all products", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<BaseResponse<Product>> getProductById(long productId) {
        try {
            var product = productHelper.getProductById(productId);
            if (product == null) {
                return new ResponseEntity<>(new BaseResponse<>(false , "Product not found", null), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(new BaseResponse<>(true, "Product with id: " + productId, product), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new BaseResponse<>(false, "Internal Server Error", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<BaseResponse<Product>> addProduct(ProductDto productDto) {
        try{
            Product product = productMapper.toEntity(productDto);
            product.setCreatedAt(LocalDateTime.now());
            product = productHelper.saveProduct(product);
            return new ResponseEntity<>(new BaseResponse<>(true, "Product Created", product), HttpStatus.CREATED);
        } catch(Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(new BaseResponse<>(false, "Error adding product: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<BaseResponse<Product>> updateProduct(long productId, ProductDto productDto) {
        try {
            Product product = productRepository.findById(productId).orElse(null);
            if (product == null) return new ResponseEntity<>(new BaseResponse<>(false, "Product not found", null), HttpStatus.NOT_FOUND);
            product.setName(productDto.getName());
            product.setPrice(productDto.getPrice());
            product.setUpdatedAt(LocalDateTime.now());
            var res = productHelper.updateProduct(product);
            return new ResponseEntity<>(new BaseResponse<>(true, "Product Updated", res), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(new BaseResponse<>(false, "Error updating product: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<BaseResponse<Product>> deleteProduct(long productId) {
        try {
            var res = productHelper.deleteProductById(productId);
            if (res == 1) {
                return new ResponseEntity<>(new BaseResponse<>(true, "Product Deleted Successfully", null), HttpStatus.OK);
            }
            return new ResponseEntity<>(new BaseResponse<>(false, "Product not found", null), HttpStatus.NOT_FOUND);
        }  catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(new BaseResponse<>(false, "Error deleting product: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<BaseResponse<Page<Product>>> listProducts(PageRequest pageable) {
        try {
            var res = productHelper.getListOfProducts(pageable);
            if (res.isEmpty()) return new ResponseEntity<>(new BaseResponse<>(true, "No Products Exists", res), HttpStatus.OK);
            return new ResponseEntity<>(new BaseResponse<>(true, "All Products", res), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(new BaseResponse<>(false, "Internal Server Error", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}