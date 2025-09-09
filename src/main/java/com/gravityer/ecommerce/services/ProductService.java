package com.gravityer.ecommerce.services;

import com.gravityer.ecommerce.controller.BaseResponse;
import com.gravityer.ecommerce.models.Product;
import com.gravityer.ecommerce.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

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
    public BaseResponse<Product> addProduct(Product product) {
        try{
            productRepository.saveAndFlush(product);
            return new BaseResponse<>(true, "Product Created", product);
        } catch(Exception e){
            log.error(e.getMessage());
            return new BaseResponse<>(false, "Error adding product: " + e.getMessage(), null);
        }
    }

    @Transactional
    public BaseResponse<Product> updateProduct(long productId, Product product) {
        try {
            var res = productRepository.updateProductById(productId, product.getProductName(), product.getDescription(), product.getPrice(), product.getQuantity());
            if (res == 1) return new BaseResponse<>(true, "Product Updated", product);
            return new BaseResponse<>(false, "Product not found", null);
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

}
