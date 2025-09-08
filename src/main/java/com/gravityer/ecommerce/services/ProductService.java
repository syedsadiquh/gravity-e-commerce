package com.gravityer.ecommerce.services;

import com.gravityer.ecommerce.controller.BaseResponse;
import com.gravityer.ecommerce.models.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private List<Product> products = new ArrayList<>();

    public BaseResponse<List<Product>> getProducts() {
        return new BaseResponse<>(true, "All Products", products);
    }

    public BaseResponse<Product> getProductById(long productId) {
        Product product = null;
        if (productId > 0) {
            for (Product p : products) {
                if (p.getId() == productId) {
                    product = p;
                    break;
                }
            }
        }
        if (product == null) {
            return new BaseResponse<>(false , "Product not found", null);
        }
        return new BaseResponse<>(true, "Product found", product);
    }

    public BaseResponse<Product> addProduct(Product product) {
        try{
            products.add(product);
            return new BaseResponse<>(true, "Product Created", product);
        } catch(Exception e){
            return new BaseResponse<>(false, "Error adding product", null);
        }
    }

    public BaseResponse<Product> updateProduct(long productId, Product product) {
        if (productId > 0) {
            for (int i = 0; i<products.size(); i++) {
                if (products.get(i).getId() == productId) {
                    products.set(i, product);
                    return new BaseResponse<>(true, "Product Updated", product);
                }
            }
        }
        return new BaseResponse<>(false, "Product not found", null);
    }

    public BaseResponse<Product> deleteProduct(long productId) {
        if (productId > 0) {
            for (int i = 0; i<products.size(); i++) {
                if (products.get(i).getId() == productId) {
                    var temp = products.get(i);
                    products.remove(i);
                    return new BaseResponse<>(true, "Product Deleted Successfully", temp);
                }
            }
        }
        return new BaseResponse<>(false, "Product not found", null);
    }

}
