package com.gravityer.ecommerce.repositories.mongo;

import com.gravityer.ecommerce.models.MongoFeedback;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface MongoFeedbackRepository extends MongoRepository<MongoFeedback, ObjectId> {
    List<MongoFeedback> findByCustomer(ObjectId customerId);

    List<MongoFeedback> findByDate(LocalDate date);
}
