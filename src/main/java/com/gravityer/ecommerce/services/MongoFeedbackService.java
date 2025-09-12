package com.gravityer.ecommerce.services;

import com.gravityer.ecommerce.controller.BaseResponse;
import com.gravityer.ecommerce.dto.AvgRatingCustomerDto;
import com.gravityer.ecommerce.dto.FeedbackPerCity;
import com.gravityer.ecommerce.dto.MongoFeedbackDto;
import com.gravityer.ecommerce.models.MongoCustomers;
import com.gravityer.ecommerce.models.MongoFeedback;
import com.gravityer.ecommerce.repositories.mongo.MongoFeedbackRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;


@Service
@RequiredArgsConstructor
public class MongoFeedbackService {
    private final MongoFeedbackRepository mongoFeedbackRepository;
    private final MongoTemplate mongoTemplate;

    public BaseResponse<List<MongoFeedback>> getAllFeedbacks() {
        try {
            var feedbacks = mongoFeedbackRepository.findAll();
            return new BaseResponse<>(true, "Feedbacks retrieved successfully", feedbacks);
        } catch (Exception e) {
            return new BaseResponse<>(false, "Error retrieving feedbacks: " + e.getMessage(), null);
        }
    }

    public BaseResponse<MongoFeedback> getFeedbackById(String feedbackId) {
        try {
            var feedback = mongoFeedbackRepository.findById(feedbackId).orElse(null);
            if (feedback == null) {
                return new BaseResponse<>(false, "Feedback not found", null);
            }
            return new BaseResponse<>(true, "Feedback retrieved successfully", feedback);
        } catch (Exception e) {
            return new BaseResponse<>(false, "Error retrieving feedback: " + e.getMessage(), null);
        }
    }

    public BaseResponse<List<MongoFeedback>> getFeedbacksByCustomerId(String customerId) {
        try {
            var feedbacks = mongoFeedbackRepository.findByCustomer(customerId);
            return new BaseResponse<>(true, "Feedbacks retrieved successfully", feedbacks);
        } catch (Exception e) {
            return new BaseResponse<>(false, "Error retrieving feedbacks: " + e.getMessage(), null);
        }
    }

    public BaseResponse<List<MongoFeedback>> getFeedbacksByDate(LocalDate date) {
        try {
            var feedbacks = mongoFeedbackRepository.findByDate(date);
            if (feedbacks == null) return new BaseResponse<>(true, "No Feedbacks found", null);
            return new BaseResponse<>(true, "Feedbacks retrieved successfully", feedbacks);
        } catch (Exception e) {
            return new BaseResponse<>(false, "Error retrieving feedbacks: " + e.getMessage(), null);
        }
    }

    @Transactional
    public BaseResponse<MongoFeedback> addFeedback(MongoFeedbackDto feedbackDto) {
        if (feedbackDto.getCustomer()==null || feedbackDto.getComment()==null || feedbackDto.getRating()==null)
            return new BaseResponse<>(false, "missing contents", null);
        try {
            var feedback = MongoFeedback.builder()
                    .customer(new ObjectId(feedbackDto.getCustomer()))
                    .comment(feedbackDto.getComment())
                    .rating(feedbackDto.getRating())
                    .createdAt(LocalDateTime.now())
                    .date(LocalDate.now())
                    .build();
            var savedFeedback = mongoFeedbackRepository.save(feedback);
            return new BaseResponse<>(true, "Feedback added successfully", savedFeedback);
        } catch (Exception e) {
            return new BaseResponse<>(false, "Error adding feedback: " + e.getMessage(), null);
        }
    }

    @Transactional
    public BaseResponse<MongoFeedback> updateFeedback(String customerId, MongoFeedbackDto feedbackDto) {
        try {
            var existingFeedback = mongoFeedbackRepository.findById(customerId).orElse(null);
            if (existingFeedback == null) {
                return new BaseResponse<>(false, "Feedback not found", null);
            }
            existingFeedback.setCustomer(new ObjectId(feedbackDto.getCustomer()));
            existingFeedback.setComment(feedbackDto.getComment());
            existingFeedback.setRating(feedbackDto.getRating());
            existingFeedback.setUpdatedAt(LocalDateTime.now());
            var updatedFeedback = mongoFeedbackRepository.save(existingFeedback);
            return new BaseResponse<>(true, "Feedback updated successfully", updatedFeedback);
        } catch (Exception e) {
            return new BaseResponse<>(false, "Error updating feedback: " + e.getMessage(), null);
        }

    }

    @Transactional
    public BaseResponse<String> deleteFeedback(String feedbackId) {
        try {
            var existingFeedback = mongoFeedbackRepository.findById(feedbackId).orElse(null);
            if (existingFeedback == null) {
                return new BaseResponse<>(false, "Feedback not found", null);
            }
            mongoFeedbackRepository.deleteById(feedbackId);
            return new BaseResponse<>(true, "Feedback deleted successfully", feedbackId);
        } catch (Exception e) {
            return new BaseResponse<>(false, "Error deleting feedback: " + e.getMessage(), null);
        }
    }

    // Get feedbacks with rating ≥ 4
    public BaseResponse<List<MongoFeedback>> getFeedbacksGreaterThanEqualToFour() {
        try {
            Criteria ratingGt4Criteria = Criteria.where("rating").gte(4);
            Query q = new Query(ratingGt4Criteria);
            var result = mongoTemplate.find(q, MongoFeedback.class);
            return new BaseResponse<>(true, "Feedbacks retrieved successfully", result);
        } catch (Exception e) {
            return new BaseResponse<>(false, "Error retrieving feedbacks: " + e.getMessage(), null);
        }
    }

    // Feedbacks from customers who belong to "city"
    public BaseResponse<List<MongoFeedback>> getFeedbacksFromCity(String city) {
        try {
            city = city.strip();
            Criteria cityCriteria = Criteria.where("city").is(city);
            Query cityQuery = new Query(cityCriteria);
            List<MongoCustomers> customersInCity = mongoTemplate.find(cityQuery, MongoCustomers.class);
            List<ObjectId> customerIds = customersInCity.stream().map(MongoCustomers::getId).toList();
            if (customerIds.isEmpty()) return new BaseResponse<>(true, "No customers found in the specified city", null);

            Criteria feedbackFromCityCustomers = Criteria.where("customer").in(customerIds);
            Query feedbackFromCityQuery = new Query(feedbackFromCityCustomers);
            var result = mongoTemplate.find(feedbackFromCityQuery, MongoFeedback.class);
            return new BaseResponse<>(true, "Feedbacks retrieved successfully", result);
        } catch (Exception e) {
            return new BaseResponse<>(false, "Error retrieving feedbacks: " + e.getMessage(), null);
        }
    }

    // Average Rating per Customer
    public BaseResponse<List<AvgRatingCustomerDto>> getAvgRatingPerCustomer() {
        try {
            GroupOperation groupByCustomer = group("customer")
                    .avg("rating").as("averageRating")
                    .first("customerDetails").as("customerInfo");

            SortOperation sortByAvgRatingDesc = sort(Sort.Direction.DESC, "averageRating");

            LookupOperation joinCustomers = LookupOperation.newLookup()
                    .from("customers")
                    .localField("customer")
                    .foreignField("_id")
                    .as("customerDetails");

            ProjectionOperation projection = project("averageRating")
                    .and("_id").as("customerId")
                    .and("customerInfo.name").as("customerName")
                    .and("customerInfo.email").as("customerEmail")
                    .and("customerInfo.city").as("customerCity");

            var aggregation = newAggregation(
                    joinCustomers,
                    unwind("customerDetails"),
                    groupByCustomer,
                    sortByAvgRatingDesc,
                    projection
            );

            var avgRatings = mongoTemplate.aggregate(aggregation, "feedbacks", AvgRatingCustomerDto.class).getMappedResults();

            return new BaseResponse<>(true, "Average ratings retrieved successfully", avgRatings);
        } catch (Exception e) {
            return new BaseResponse<>(false, "Error retrieving average ratings: " + e.getMessage(), null);
        }
    }

    // total feedbacks per city
    public BaseResponse<List<FeedbackPerCity>> getTotalFeedbacksPerCity() {
        try {
            LookupOperation joinCustomers = LookupOperation.newLookup()
                    .from("customers")
                    .localField("customer")
                    .foreignField("_id")
                    .as("customerDetails");

            UnwindOperation unwindCustomerDetails = unwind("customerDetails");

            GroupOperation groupByCity = group("customerDetails.city")
                    .count().as("feedbackCount");

            ProjectionOperation projection = project("feedbackCount")
                    .and("_id").as("city");

            var aggregation = newAggregation(
                    joinCustomers,
                    unwindCustomerDetails,
                    groupByCity,
                    projection
            );

            var feedbacksPerCity = mongoTemplate.aggregate(aggregation, "feedbacks", FeedbackPerCity.class).getMappedResults();

            return new BaseResponse<>(true, "Feedbacks per city retrieved successfully", feedbacksPerCity);
        } catch (Exception e) {
            return new BaseResponse<>(false, "Error retrieving feedbacks per city: " + e.getMessage(), null);
        }
    }

}

