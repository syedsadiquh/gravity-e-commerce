package com.gravityer.ecommerce.models;

import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Document(collection = "feedbacks")
public class MongoFeedback extends BaseEntity {
    @Id
    private String id;

    private String customer;

    @Min(1) @Max(5)
    private Integer rating;

    private String comment;

    private LocalDate date;

}
