package com.gravityer.ecommerce.services;

import com.gravityer.ecommerce.controller.BaseResponse;
import com.gravityer.ecommerce.dto.AvgRatingCustomerDto;
import com.gravityer.ecommerce.dto.FeedbackPerCity;
import com.gravityer.ecommerce.dto.MongoFeedbackDto;
import com.gravityer.ecommerce.exceptions.ItemNotFoundException;
import com.gravityer.ecommerce.models.MongoCustomers;
import com.gravityer.ecommerce.models.MongoFeedback;
import com.gravityer.ecommerce.repositories.mongo.MongoCustomerRepository;
import com.gravityer.ecommerce.repositories.mongo.MongoFeedbackRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;


@Service
@RequiredArgsConstructor
public class MongoFeedbackService {
    private final MongoFeedbackRepository mongoFeedbackRepository;
    private final MongoTemplate mongoTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    private final MongoCustomerRepository mongoCustomerRepository;

    public ResponseEntity<BaseResponse<List<MongoFeedback>>> getAllFeedbacks() {
        try {
            var feedbacks = mongoFeedbackRepository.findAll();
            if  (feedbacks.isEmpty()) {
                return new ResponseEntity<>(new BaseResponse<>(false, "No Feedbacks found", feedbacks), HttpStatus.OK);
            }
            return new ResponseEntity<>(new BaseResponse<>(true, "Feedbacks retrieved successfully", feedbacks), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new BaseResponse<>(false, "Error retrieving feedbacks: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<BaseResponse<MongoFeedback>> getFeedbackById(String feedbackId) {
        try {
            var feedback = mongoFeedbackRepository.findById(new ObjectId(feedbackId)).orElseThrow(
                    () -> new ItemNotFoundException("Feedback not found with id: " + feedbackId)
            );
            return new ResponseEntity<>(new BaseResponse<>(true, "Feedback retrieved successfully", feedback), HttpStatus.OK);
        } catch (ItemNotFoundException e) {
            throw e;
        } catch (Exception e) {
            return new ResponseEntity<>(new BaseResponse<>(false, "Error retrieving feedback: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<BaseResponse<List<MongoFeedback>>> getFeedbacksByCustomerId(String customerId) {
        try {
            var customer = mongoCustomerRepository.findById(new ObjectId(customerId)).orElseThrow(
                    () -> new ItemNotFoundException("Customer not found with id: " + customerId)
            );
            var feedbacks = mongoFeedbackRepository.findByCustomer(new ObjectId(customerId));
            return new ResponseEntity<>(new BaseResponse<>(true, "Feedbacks retrieved successfully", feedbacks), HttpStatus.OK);
        } catch (ItemNotFoundException e) {
            throw e;
        } catch (Exception e) {
            return new ResponseEntity<>(new BaseResponse<>(false, "Error retrieving feedbacks: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<BaseResponse<List<MongoFeedback>>> getFeedbacksByDate(LocalDate date) {
        try {
            var feedbacks = mongoFeedbackRepository.findByDate(date);
            if (feedbacks.isEmpty()) {
                return new ResponseEntity<>(new BaseResponse<>(false, "Feedbacks not found", feedbacks), HttpStatus.OK);
            }
            return new ResponseEntity<>(new BaseResponse<>(true, "Feedbacks retrieved successfully", feedbacks), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new BaseResponse<>(false, "Error retrieving feedbacks: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<BaseResponse<MongoFeedback>> addFeedback(MongoFeedbackDto feedbackDto) {
        if (feedbackDto.getCustomer()==null || feedbackDto.getComment()==null || feedbackDto.getRating()==null)
            return new ResponseEntity<>(new BaseResponse<>(false, "missing contents", null), HttpStatus.BAD_REQUEST);
        try {
            var feedback = MongoFeedback.builder()
                    .customer(new ObjectId(feedbackDto.getCustomer()))
                    .comment(feedbackDto.getComment())
                    .rating(feedbackDto.getRating())
                    .createdAt(LocalDateTime.now())
                    .date(LocalDate.now())
                    .build();
            var savedFeedback = mongoFeedbackRepository.save(feedback);
            return new ResponseEntity<>(new BaseResponse<>(true, "Feedback added successfully", savedFeedback), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new BaseResponse<>(false, "Error adding feedback: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<BaseResponse<MongoFeedback>> updateFeedback(String feedbackId, MongoFeedbackDto feedbackDto) {
        try {
            var existingFeedback = mongoFeedbackRepository.findById(new ObjectId(feedbackId)).orElseThrow(
                    () -> new ItemNotFoundException("Customer not found with id: " + feedbackId)
            );
            existingFeedback.setCustomer(new ObjectId(feedbackDto.getCustomer()));
            existingFeedback.setComment(feedbackDto.getComment());
            existingFeedback.setRating(feedbackDto.getRating());
            existingFeedback.setUpdatedAt(LocalDateTime.now());
            var updatedFeedback = mongoFeedbackRepository.save(existingFeedback);
            return new ResponseEntity<>(new BaseResponse<>(true, "Feedback updated successfully", updatedFeedback), HttpStatus.OK);
        } catch (ItemNotFoundException e) {
            throw e;
        } catch (Exception e) {
            return new ResponseEntity<>(new BaseResponse<>(false, "Error updating feedback: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Transactional
    public ResponseEntity<BaseResponse<String>> deleteFeedback(String feedbackId) {
        try {
            var existingFeedback = mongoFeedbackRepository.findById(new ObjectId(feedbackId)).orElseThrow(
                    () -> new ItemNotFoundException("Feedback not found with id: " + feedbackId)
            );
            mongoFeedbackRepository.deleteById(new ObjectId(feedbackId));
            return new ResponseEntity<>(new BaseResponse<>(true, "Feedback deleted successfully", feedbackId), HttpStatus.OK);
        } catch (ItemNotFoundException e) {
            throw e;
        } catch (Exception e) {
            return new ResponseEntity<>(new BaseResponse<>(false, "Error deleting feedback: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get feedbacks with rating ≥ 4
    public ResponseEntity<BaseResponse<List<MongoFeedback>>> getFeedbacksGreaterThanEqualToFour() {
        try {
            Criteria ratingGt4Criteria = Criteria.where("rating").gte(4);
            Query q = new Query(ratingGt4Criteria);
            var result = mongoTemplate.find(q, MongoFeedback.class);
            return new ResponseEntity<>(new BaseResponse<>(true, "Feedbacks retrieved successfully", result), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new BaseResponse<>(false, "Error retrieving feedbacks: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Feedbacks from customers who belong to "city"
    public ResponseEntity<BaseResponse<List<MongoFeedback>>> getFeedbacksFromCity(String city) {
        try {
            city = city.strip();

            if (redisTemplate.opsForHash().hasKey("FeedbackCache::feedbackFromCity", city)) {
                var cachedData = (List<MongoFeedback>) redisTemplate.opsForHash().get("FeedbackCache::feedbackFromCity:", city);
                if (cachedData != null) {
                    return new ResponseEntity<>(new BaseResponse<>(true, "Feedbacks retrieved successfully (from cache)", cachedData), HttpStatus.OK);
                }
            }

            Criteria cityCriteria = Criteria.where("city").is(city);
            Query cityQuery = new Query(cityCriteria);
            List<MongoCustomers> customersInCity = mongoTemplate.find(cityQuery, MongoCustomers.class);
            List<ObjectId> customerIds = customersInCity.stream().map(MongoCustomers::getId).toList();
            if (customerIds.isEmpty()) return new ResponseEntity<>(new BaseResponse<>(false, "Customers not found in the specified city", null), HttpStatus.NOT_FOUND);

            Criteria feedbackFromCityCustomers = Criteria.where("customer").in(customerIds);
            Query feedbackFromCityQuery = new Query(feedbackFromCityCustomers);
            var result = mongoTemplate.find(feedbackFromCityQuery, MongoFeedback.class);

            redisTemplate.opsForHash().put("FeedbackCache::feedbackFromCity", city, result);
            redisTemplate.opsForHash().expire("FeedbackCache::feedbackFromCity:", Duration.ofMinutes(5), Collections.singleton(city));

            return new ResponseEntity<>(new BaseResponse<>(true, "Feedbacks retrieved successfully", result), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new BaseResponse<>(false, "Error retrieving feedbacks: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Average Rating per Customer
    public ResponseEntity<BaseResponse<List<AvgRatingCustomerDto>>> getAvgRatingPerCustomer() {
        try {
            if (redisTemplate.hasKey("FeedbackCache::avgRatingsPerCustomer")) {
                var cachedData = (List<AvgRatingCustomerDto>) redisTemplate.opsForValue().get("FeedbackCache::avgRatingsPerCustomer");
                if (cachedData != null) {
                    return new ResponseEntity<>(new BaseResponse<>(true, "Average ratings retrieved successfully (from cache)", cachedData), HttpStatus.OK);
                }
            }
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

            redisTemplate.opsForValue().set("FeedbackCache::avgRatingsPerCustomer", avgRatings, Duration.ofMinutes(5));

            return new ResponseEntity<>(new BaseResponse<>(true, "Average ratings retrieved successfully", avgRatings), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new BaseResponse<>(false, "Error retrieving average ratings: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // total feedbacks per city
    public ResponseEntity<BaseResponse<List<FeedbackPerCity>>> getTotalFeedbacksPerCity() {
        try {
            if (redisTemplate.hasKey("FeedbackCache::totalFeedbacksPerCity")) {
                var cachedData = (List<FeedbackPerCity>) redisTemplate.opsForValue().get("FeedbackCache::totalFeedbacksPerCity");
                if (cachedData != null) {
                    return new ResponseEntity<>(new BaseResponse<>(true, "Feedbacks per city retrieved successfully (from cache)", cachedData), HttpStatus.OK);
                }
            }
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

            redisTemplate.opsForValue().set("FeedbackCache::totalFeedbacksPerCity", feedbacksPerCity, Duration.ofMinutes(5));

            return new ResponseEntity<>(new BaseResponse<>(true, "Feedbacks per city retrieved successfully", feedbacksPerCity), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new BaseResponse<>(false, "Error retrieving feedbacks per city: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

