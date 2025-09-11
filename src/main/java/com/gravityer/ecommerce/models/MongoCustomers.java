package com.gravityer.ecommerce.models;

import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Document(collection = "customers")
public class MongoCustomers extends BaseEntity {
    @Id
    private String id;
    private String name;
    @Indexed(unique = true)
    private String email;
    private String city;
}
