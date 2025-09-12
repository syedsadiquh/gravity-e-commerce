package com.gravityer.ecommerce.repositories.mongo;

import com.gravityer.ecommerce.models.MongoCustomers;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoCustomerRepository extends MongoRepository<MongoCustomers, ObjectId> {
}
