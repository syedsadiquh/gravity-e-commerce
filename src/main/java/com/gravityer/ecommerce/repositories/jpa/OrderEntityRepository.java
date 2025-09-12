package com.gravityer.ecommerce.repositories.jpa;

import com.gravityer.ecommerce.dto.CustomerSpendDto;
import com.gravityer.ecommerce.dto.ProductSalesDto;
import com.gravityer.ecommerce.models.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderEntityRepository extends JpaRepository<OrderEntity, Long> {
    
    // Join to get all details in details
    @Query("SELECT o FROM orders o " +
           "JOIN FETCH o.customer " +
           "LEFT JOIN FETCH o.items i " +
           "LEFT JOIN FETCH i.product")
    List<OrderEntity> findAllOrdersWithCustomerAndProducts();
    
    // joining orders,orderitms and products and grouping by customer and then aggregating
    @Query("SELECT new com.gravityer.ecommerce.dto.CustomerSpendDto(" +
           "c.id, c.name, c.email, SUM(oi.quantity * p.price)) " +
           "FROM customers c " +
           "JOIN orders o ON o.customer.id = c.id " +
           "JOIN order_items oi ON oi.orders.id = o.id " +
           "JOIN products p ON oi.product.id = p.id " +
           "GROUP BY c.id, c.name, c.email " +
           "ORDER BY SUM(oi.quantity * p.price) DESC")
    List<CustomerSpendDto> getTotalSpendPerCustomer();
    
    // joining orderitems grouping by product and then aggregating
    @Query("SELECT new com.gravityer.ecommerce.dto.ProductSalesDto(" +
           "p.id, p.name, p.price, SUM(oi.quantity), SUM(oi.quantity * p.price)) " +
           "FROM products p " +
           "JOIN order_items oi ON oi.product.id = p.id " +
           "GROUP BY p.id, p.name, p.price " +
           "ORDER BY SUM(oi.quantity * p.price) DESC")
    List<ProductSalesDto> getTotalSalesPerProduct();

    List<OrderEntity> findByCustomerId(Long customerId);
}
