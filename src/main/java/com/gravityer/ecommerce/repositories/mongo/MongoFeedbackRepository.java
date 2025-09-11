package com.gravityer.ecommerce.repositories.mongo;

import com.gravityer.ecommerce.models.MongoFeedback;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MongoFeedbackRepository extends MongoRepository<MongoFeedback, String> {
    List<MongoFeedback> findByCustomer(String customerId);
}
