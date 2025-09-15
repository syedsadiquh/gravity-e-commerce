package com.gravityer.ecommerce.services;

import com.gravityer.ecommerce.models.Product;
import com.gravityer.ecommerce.repositories.jpa.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductHelper {
    private final ProductRepository productRepository;

    @Cacheable(value = "ProductCache", key = "'allProducts'")
    public List<Product> getProductsImp() {
        return productRepository.findAll();
    }

    @Cacheable(value = "ProductCache::product", key = "#id")
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @CachePut(value = "ProductCache::product", key = "#product.id")
    @CacheEvict(value = "ProductCache", key = "'allProducts'")
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @CachePut(value = "ProductCache::product", key = "#product.id")
    @CacheEvict(value = "ProductCache", key = "'allProducts'")
    public Product updateProduct(Product product) {
        return productRepository.saveAndFlush(product);
    }

    @Caching(
            evict = {
                @CacheEvict(value = "ProductCache::product", key = "#id"),
                @CacheEvict(value = "ProductCache", key = "'allProducts'"),
            }
    )
    public int deleteProductById(Long id) {
        return productRepository.deleteProductById(id);
    }

    @Cacheable(value = "ProductCache", key = "'listProducts:'+#pageable.getOffset()+':'+#pageable.getPageSize()")
    public Page<Product> getListOfProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
}
