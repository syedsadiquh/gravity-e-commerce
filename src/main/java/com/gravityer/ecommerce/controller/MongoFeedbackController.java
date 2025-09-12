package com.gravityer.ecommerce.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gravityer.ecommerce.dto.AvgRatingCustomerDto;
import com.gravityer.ecommerce.dto.FeedbackPerCity;
import com.gravityer.ecommerce.dto.MongoFeedbackDto;
import com.gravityer.ecommerce.models.BaseEntity;
import com.gravityer.ecommerce.models.MongoFeedback;
import com.gravityer.ecommerce.services.MongoFeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MongoFeedbackController {
    private final MongoFeedbackService feedbackService;

    @GetMapping("/mongo/getAllFeedbacks")
    public ResponseEntity<BaseResponse<List<MongoFeedback>>> getAllFeedbacks() {
        var res = feedbackService.getAllFeedbacks();
        if (res.isSuccess()) return ResponseEntity.ok(res);
        return ResponseEntity.internalServerError().body(res);
    }

    @GetMapping("/mongo/getFeedback/{feedbackId}")
    public ResponseEntity<BaseResponse<MongoFeedback>> getFeedbackById(@PathVariable String feedbackId) {
        var res = feedbackService.getFeedbackById(feedbackId);
        if (res.isSuccess()) return ResponseEntity.ok(res);
        if (res.getMessage().contains("not found")) return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        return ResponseEntity.internalServerError().body(res);
    }

    @GetMapping("/mongo/getFeedbacksByCustomer/{customerId}")
    public ResponseEntity<BaseResponse<List<MongoFeedback>>> getFeedbacksByCustomerId(@PathVariable  String customerId) {
        var res = feedbackService.getFeedbacksByCustomerId(customerId);
        if (res.isSuccess()) return ResponseEntity.ok(res);
        if (res.getMessage().contains("not found")) return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        return ResponseEntity.internalServerError().body(res);
    }

    @GetMapping("/mongo/getFeedbacksByDate/{date}")
    public ResponseEntity<BaseResponse<List<MongoFeedback>>> getFeedbacksByDate(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        var res = feedbackService.getFeedbacksByDate(localDate);
        if (res.isSuccess()) return ResponseEntity.ok(res);
        return ResponseEntity.internalServerError().body(res);
    }

    @PostMapping("/mongo/addFeedback")
    public ResponseEntity<BaseResponse<MongoFeedback>> addFeedback(@Valid @RequestBody MongoFeedbackDto feedbackDto) {
        var res = feedbackService.addFeedback(feedbackDto);
        if (res.isSuccess()) return ResponseEntity.ok(res);
        if (res.getMessage().contains("missing")) return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        return ResponseEntity.internalServerError().body(res);
    }

    @PutMapping("/mongo/updateFeedback/{feedbackId}")
    public ResponseEntity<BaseResponse<MongoFeedback>> updateFeedback(@PathVariable String feedbackId, @RequestBody MongoFeedbackDto feedbackDto) {
        var res = feedbackService.updateFeedback(feedbackId, feedbackDto);
        if (res.isSuccess()) return ResponseEntity.ok(res);
        if (res.getMessage().contains("not found")) return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        return ResponseEntity.internalServerError().body(res);
    }

    @DeleteMapping("/mongo/deleteFeedback/{feedbackId}")
    public ResponseEntity<BaseResponse<String>> deleteFeedback(@PathVariable String feedbackId) {
        var res = feedbackService.deleteFeedback(feedbackId);
        if (res.isSuccess()) return ResponseEntity.ok(res);
        if (res.getMessage().contains("not found")) return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        return ResponseEntity.internalServerError().body(res);
    }

    @GetMapping("/mongo/getFeedbackGteFour")
    public ResponseEntity<BaseResponse<List<MongoFeedback>>> getFeedbackGteFour() {
        var res = feedbackService.getFeedbacksGreaterThanEqualToFour();
        if (res.isSuccess()) return ResponseEntity.ok(res);
        return ResponseEntity.internalServerError().body(res);
    }

    @GetMapping("/mongo/getFeedbackFromCity/{city}")
    public ResponseEntity<BaseResponse<List<MongoFeedback>>> getFeedbackFromCity(@PathVariable String city) {
        var res = feedbackService.getFeedbacksFromCity(city);
        if (res.isSuccess()) return ResponseEntity.ok(res);
        return ResponseEntity.internalServerError().body(res);
    }

    @GetMapping("/mongo/getAvgRatingPerCustomer")
    public ResponseEntity<BaseResponse<List<AvgRatingCustomerDto>>> getAvgRatingPerCustomer() {
        var res = feedbackService.getAvgRatingPerCustomer();
        if (res.isSuccess()) return ResponseEntity.ok(res);
        return ResponseEntity.internalServerError().body(res);
    }

    @GetMapping("/mongo/getFeedbackCountPerCity")
    public ResponseEntity<BaseResponse<List<FeedbackPerCity>>> getFeedbackCountPerCity() {
        var res = feedbackService.getTotalFeedbacksPerCity();
        if (res.isSuccess()) return ResponseEntity.ok(res);
        return ResponseEntity.internalServerError().body(res);
    }

}
