package org.prgrms.coffee_order_be.product.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    @NotNull
    private UUID productId;
    @NotNull
    private String productName;
    @NotNull
    private String category;
    @NotNull
    private long price;

    private String description;
}
