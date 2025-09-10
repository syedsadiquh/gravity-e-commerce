package com.gravityer.ecommerce.repositories;

import com.gravityer.ecommerce.models.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE products p SET p.name=:productName, p.price=:price WHERE p.id=:id")
    public int updateProductById(Long id, String productName, double price);

    public int deleteProductById(Long id);

}
