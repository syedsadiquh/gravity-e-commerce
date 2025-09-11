package com.gravityer.ecommerce.controller;

import com.gravityer.ecommerce.dto.MongoFeedbackDto;
import com.gravityer.ecommerce.models.MongoFeedback;
import com.gravityer.ecommerce.services.MongoFeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
