package com.gravityer.ecommerce.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Entity(name = "order_items")
@Table(
        indexes = {
                @Index(name = "idx_order_items_orders_id", columnList = "orders_id"),
                @Index(name = "idx_order_items_product_id", columnList = "product_id")
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class OrderItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "orders_id")
    private OrderEntity orders;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int  quantity;
}
