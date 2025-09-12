package com.gravityer.ecommerce.repositories.mongo;

import com.gravityer.ecommerce.models.MongoCustomers;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoCustomerRepository extends MongoRepository<MongoCustomers, String> {
}
