package com.gravityer.ecommerce.services;

import com.gravityer.ecommerce.controller.BaseResponse;
import com.gravityer.ecommerce.dto.ProductDto;
import com.gravityer.ecommerce.mapper.ProductMapper;
import com.gravityer.ecommerce.models.Product;
import com.gravityer.ecommerce.repositories.jpa.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ResponseEntity<BaseResponse<List<Product>>> getProducts() {
        try {
            return new ResponseEntity<>(new BaseResponse<>(true, "All Products", productRepository.findAll()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new BaseResponse<>(false, "Unable to get all products", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<BaseResponse<Product>> getProductById(long productId) {
        try {
            var product = productRepository.findById(productId).orElse(null);
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
            productRepository.saveAndFlush(product);
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
            var res = productRepository.saveAndFlush(product);
            return new ResponseEntity<>(new BaseResponse<>(true, "Product Updated", res), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(new BaseResponse<>(false, "Error updating product: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<BaseResponse<Product>> deleteProduct(long productId) {
        try {
            var res = productRepository.deleteProductById(productId);
            if (res == 1) return new ResponseEntity<>(new BaseResponse<>(true, "Product Deleted Successfully", null), HttpStatus.OK);
            return new ResponseEntity<>(new BaseResponse<>(false, "Product not found", null), HttpStatus.NOT_FOUND);
        }  catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(new BaseResponse<>(false, "Error deleting product: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<BaseResponse<Page<Product>>> listProducts(PageRequest pageable) {
        try {
            var res = productRepository.findAll(pageable);
            if (res.isEmpty()) return new ResponseEntity<>(new BaseResponse<>(true, "No Products Exists", res), HttpStatus.OK);
            return new ResponseEntity<>(new BaseResponse<>(true, "All Products", res), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(new BaseResponse<>(false, "Internal Server Error", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
