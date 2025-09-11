package com.gravityer.ecommerce.services;

import com.gravityer.ecommerce.controller.BaseResponse;
import com.gravityer.ecommerce.dto.MongoFeedbackDto;
import com.gravityer.ecommerce.models.MongoFeedback;
import com.gravityer.ecommerce.repositories.mongo.MongoFeedbackRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MongoFeedbackService {
    private final MongoFeedbackRepository mongoFeedbackRepository;

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
                    .customer(feedbackDto.getCustomer())
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
            existingFeedback.setCustomer(feedbackDto.getCustomer());
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

}
