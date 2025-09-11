package com.gravityer.ecommerce.models;

import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "feedbacks")
public class MongoFeedback {
    @Id
    private String id;

    private String customer;

    @Min(1) @Max(5)
    private Integer rating;

    private String comment;

    private LocalDate date;

}
