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
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/addProduct")
    public ResponseEntity<BaseResponse<Product>> addProduct(@Valid @RequestBody ProductDto productDto) {
        return productService.addProduct(productDto);
    }


    // Get All Products
    @Operation(
            summary = "Get all products",
            description = "Retrieve a list of all products in the system"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of products"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/getAllProducts")
    public ResponseEntity<BaseResponse<List<Product>>> getAllProducts() {
        return productService.getProducts();
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
        return productService.getProductById(productId);
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
       return productService.updateProduct(productId, productDto);
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
        return productService.deleteProduct(productId);
    }

    // List Products (by paging and sorting)
    @Operation(
            summary = "List products with pagination and sorting",
            description = "Retrieve a paginated and sorted list of products"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of products"),
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
        return productService.listProducts(pageable);
    }

}
