package org.prgrms.coffee_order_be.product.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.coffee_order_be.common.BaseEntity;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
@Entity
public class Product extends BaseEntity {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID productId;

    @NotNull
    @Column(length = 20)
    private String productName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private Category category;

    @NotNull
    private long price;

    @Column(length = 500)
    private String description;

    public void updatePrice(long price) {
        this.price = price;
    }

    public void updateDescription(String description) {
        this.description = description;
    }
}
