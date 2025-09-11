package com.gravityer.ecommerce.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MongoFeedbackDto {
    @NotBlank(message = "Customer id is required")
    private String customer;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    @NotBlank(message = "Comment cannot be empty")
    @Size(max = 200, message = "Comment must not exceed 200 characters")
    private String comment;
}
