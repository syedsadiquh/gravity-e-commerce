package com.gravityer.ecommerce.controller;

import com.gravityer.ecommerce.dto.AvgRatingCustomerDto;
import com.gravityer.ecommerce.dto.FeedbackPerCity;
import com.gravityer.ecommerce.dto.MongoFeedbackDto;
import com.gravityer.ecommerce.models.MongoFeedback;
import com.gravityer.ecommerce.services.MongoFeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Feedback Controller", description = "APIs for Feedback Management")
public class MongoFeedbackController {
    private final MongoFeedbackService feedbackService;

    // Getting all Feedbacks
    @Operation(
            summary = "Get All Feedbacks",
            description = "Get all feedbacks in the systems"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all the feedbacks"),
            @ApiResponse(responseCode = "404", description = "Feedbacks not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/mongo/getAllFeedbacks")
    public ResponseEntity<BaseResponse<List<MongoFeedback>>> getAllFeedbacks() {
        var res = feedbackService.getAllFeedbacks();
        if (res.isSuccess()) return ResponseEntity.ok(res);
        if (res.getMessage().contains("not found")) return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        return ResponseEntity.internalServerError().body(res);
    }

    @Operation(
            summary = "Get Feedback by ID",
            description = "Fetches a feedback by ID from the MongoDB collection"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved a feedback"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/mongo/getFeedback/{feedbackId}")
    public ResponseEntity<BaseResponse<MongoFeedback>> getFeedbackById(@PathVariable String feedbackId) {
        var res = feedbackService.getFeedbackById(feedbackId);
        if (res.isSuccess()) return ResponseEntity.ok(res);
        if (res.getMessage().contains("not found")) return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        return ResponseEntity.internalServerError().body(res);
    }

    @Operation(
            summary = "Get Feedbacks by Customer ID",
            description = "Get a list of feedbacks using a customer ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all feedbacks from a customer"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/mongo/getFeedbacksByCustomer/{customerId}")
    public ResponseEntity<BaseResponse<List<MongoFeedback>>> getFeedbacksByCustomerId(@PathVariable  String customerId) {
        var res = feedbackService.getFeedbacksByCustomerId(customerId);
        if (res.isSuccess()) return ResponseEntity.ok(res);
        if (res.getMessage().contains("not found")) return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        return ResponseEntity.internalServerError().body(res);
    }

    @Operation(
            summary = "Get feedbacks by Date",
            description = "Get a list of feedbacks made on a date"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved a list of feedbacks on a date"),
            @ApiResponse(responseCode = "404", description = "Feedbacks not found"),
            @ApiResponse(responseCode = "500", description = "Internal Sever Error")
    })
    @GetMapping("/mongo/getFeedbacksByDate/{date}")
    public ResponseEntity<BaseResponse<List<MongoFeedback>>> getFeedbacksByDate(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        var res = feedbackService.getFeedbacksByDate(localDate);
        if (res.isSuccess()) return ResponseEntity.ok(res);
        if (res.getMessage().contains("not found")) return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        return ResponseEntity.internalServerError().body(res);
    }

    @Operation(
            summary = "Add a Feedback",
            description = "Add a Feedback to the Feedback system"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Added a feedback successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input date"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/mongo/addFeedback")
    public ResponseEntity<BaseResponse<MongoFeedback>> addFeedback(@Valid @RequestBody MongoFeedbackDto feedbackDto) {
        var res = feedbackService.addFeedback(feedbackDto);
        if (res.isSuccess()) return ResponseEntity.ok(res);
        if (res.getMessage().contains("missing")) return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        return ResponseEntity.internalServerError().body(res);
    }

    @Operation(
            summary = "Update an existing Feedback",
            description = "Update an existing feedback by its ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully updated a feedback"),
            @ApiResponse(responseCode = "404", description = "Feedback not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping("/mongo/updateFeedback/{feedbackId}")
    public ResponseEntity<BaseResponse<MongoFeedback>> updateFeedback(@PathVariable String feedbackId, @Valid @RequestBody MongoFeedbackDto feedbackDto) {
        var res = feedbackService.updateFeedback(feedbackId, feedbackDto);
        if (res.isSuccess()) return ResponseEntity.ok(res);
        if (res.getMessage().contains("not found")) return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        return ResponseEntity.internalServerError().body(res);
    }

    @Operation(
            summary = "Delete a Feedback",
            description = "Delete a feedback by its ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully deleted a feedback"),
            @ApiResponse(responseCode = "404", description = "Feedback not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @DeleteMapping("/mongo/deleteFeedback/{feedbackId}")
    public ResponseEntity<BaseResponse<String>> deleteFeedback(@PathVariable String feedbackId) {
        var res = feedbackService.deleteFeedback(feedbackId);
        if (res.isSuccess()) return ResponseEntity.ok(res);
        if (res.getMessage().contains("not found")) return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        return ResponseEntity.internalServerError().body(res);
    }

    @Operation(
            summary = "Get Feedbacks with rating greater than equal to four",
            description = "Get a list of feedbacks that have a rating greater than four"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved feedbacks having rating greater than four"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/mongo/getFeedbackGteFour")
    public ResponseEntity<BaseResponse<List<MongoFeedback>>> getFeedbackGteFour() {
        var res = feedbackService.getFeedbacksGreaterThanEqualToFour();
        if (res.isSuccess()) return ResponseEntity.ok(res);
        return ResponseEntity.internalServerError().body(res);
    }

    @Operation(
            summary = "Get feedbacks from a city",
            description = "Get a list of feedbacks that are from a particular city"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved feedback from a city"),
            @ApiResponse(responseCode = "404", description = "No Customers found in city"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/mongo/getFeedbackFromCity/{city}")
    public ResponseEntity<BaseResponse<List<MongoFeedback>>> getFeedbackFromCity(@PathVariable String city) {
        var res = feedbackService.getFeedbacksFromCity(city);
        if (res.isSuccess()) return ResponseEntity.ok(res);
        if (res.getMessage().contains("not found")) return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        return ResponseEntity.internalServerError().body(res);
    }

    @Operation(
            summary = "Get Average Rating per Customer",
            description = "Get a list of Customers with their average ratings"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the average rating per customer"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/mongo/getAvgRatingPerCustomer")
    public ResponseEntity<BaseResponse<List<AvgRatingCustomerDto>>> getAvgRatingPerCustomer() {
        var res = feedbackService.getAvgRatingPerCustomer();
        if (res.isSuccess()) return ResponseEntity.ok(res);
        return ResponseEntity.internalServerError().body(res);
    }

    @Operation(
            summary = "Get Feedback count per city",
            description = "Get a list of city with their feedback counts"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved a list of city with feedback counts"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/mongo/getFeedbackCountPerCity")
    public ResponseEntity<BaseResponse<List<FeedbackPerCity>>> getFeedbackCountPerCity() {
        var res = feedbackService.getTotalFeedbacksPerCity();
        if (res.isSuccess()) return ResponseEntity.ok(res);
        return ResponseEntity.internalServerError().body(res);
    }

}
