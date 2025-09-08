package com.gravityer.ecommerce.controller;

import com.gravityer.ecommerce.models.Product;
import com.gravityer.ecommerce.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    // Add Product
    @PostMapping("/addProduct")
    public ResponseEntity<BaseResponse<Product>> addProduct(@RequestBody Product product) {
        var response = productService.addProduct(product);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Get All Products
    @GetMapping("/getAllProducts")
    public ResponseEntity<BaseResponse<List<Product>>> getAllProducts() {
        var response = productService.getProducts();
        if (response.isSuccess()) return ResponseEntity.ok(response);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Get Product by ID
    @GetMapping("/getProductById/{productId}")
    public ResponseEntity<BaseResponse<Product>> getProductById(@PathVariable long productId) {
        var response = productService.getProductById(productId);
        if (response.isSuccess()) return ResponseEntity.ok(response);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Update Product
    @PutMapping("/updateProduct/{productId}")
    public ResponseEntity<BaseResponse<Product>> updateProduct(@PathVariable long productId, @RequestBody Product product) {
        var response = productService.updateProduct(productId, product);
        if (response.isSuccess()) return ResponseEntity.ok(response);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Delete Product
    @DeleteMapping("/deleteProduct/{productId}")
    public ResponseEntity<BaseResponse<Product>> deleteProduct(@PathVariable long productId) {
        var response = productService.deleteProduct(productId);
        if (response.isSuccess()) return ResponseEntity.ok(response);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
