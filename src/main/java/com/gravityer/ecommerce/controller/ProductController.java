package com.gravityer.ecommerce.controller;

import com.gravityer.ecommerce.dto.ProductDto;
import com.gravityer.ecommerce.models.Product;
import com.gravityer.ecommerce.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Product Controller", description = "APIs for managing products")
public class ProductController {

    private final ProductService productService;

    // Add Product
    @Operation(
            summary = "Add a new product",
            description = "Add a new Product to the system"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/addProduct")
    public ResponseEntity<BaseResponse<Product>> addProduct(@Valid @RequestBody ProductDto productDto) {
        var response = productService.addProduct(productDto);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        if(response.getMessage().contains("Error")) {
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    // Get All Products
    @Operation(
            summary = "Get all products",
            description = "Retrieve a list of all products in the system"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of products"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/getAllProducts")
    public ResponseEntity<BaseResponse<List<Product>>> getAllProducts() {
        var response = productService.getProducts();
        if (response.isSuccess()) return ResponseEntity.ok(response);
        if(response.getMessage().contains("Error")) return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    // Get Product by ID
    @Operation(
            summary = "Get a product by ID",
            description = "Retrieve a Product by its ID from the system"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the product"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/getProductById/{productId}")
    public ResponseEntity<BaseResponse<Product>> getProductById(@PathVariable long productId) {
        var response = productService.getProductById(productId);
        if (response.isSuccess()) return ResponseEntity.ok(response);
        if (response.getMessage().contains("not found")) return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        if(response.getMessage().contains("Error")) return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Update Product
    @Operation(
            summary = "Update an existing product",
            description = "Update the details of an existing Product in the system"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully updated the product"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/updateProduct/{productId}")
    public ResponseEntity<BaseResponse<Product>> updateProduct(@PathVariable long productId, @RequestBody ProductDto productDto) {
        var response = productService.updateProduct(productId, productDto);
        if (response.isSuccess()) return ResponseEntity.ok(response);
        if (response.getMessage().contains("not found")) return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        if (response.getMessage().contains("Error")) return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Delete Product
    @Operation(
            summary = "Delete a product",
            description = "Delete a Product from the system by its ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully deleted the product"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/deleteProduct/{productId}")
    public ResponseEntity<BaseResponse<Product>> deleteProduct(@PathVariable long productId) {
        var response = productService.deleteProduct(productId);
        if (response.isSuccess()) return ResponseEntity.ok(response);
        if (response.getMessage().contains("not found")) return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        if (response.getMessage().contains("Error")) return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // List Products (by paging and sorting)
    @Operation(
            summary = "List products with pagination and sorting",
            description = "Retrieve a paginated and sorted list of products"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of products"),
            @ApiResponse(responseCode = "404", description = "No products found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/listProducts")
    public ResponseEntity<BaseResponse<Page<Product>>> listProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending
    ) {
        var sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        var pageable = PageRequest.of(page, size, sort);
        var res = productService.listProducts(pageable);
        if (res.isSuccess()) {
            return new ResponseEntity<>(res, HttpStatus.OK);
        } else {
            if (res.getMessage().equals("No Products Exists")) {
                return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
