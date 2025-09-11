package com.gravityer.ecommerce.repositories.jpa;

import com.gravityer.ecommerce.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c FROM customers c WHERE c.id IN " +
           "(SELECT o.customer.id FROM orders o GROUP BY o.customer.id HAVING COUNT(o) > 3)")
    List<Customer> findCustomersWithMoreThanThreeOrders();
}
