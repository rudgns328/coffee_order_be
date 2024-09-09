package org.prgrms.coffee_order_be.order.model.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.coffee_order_be.product.domain.Category;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponseDTO {

    @NotNull
    private UUID productId;
    @NotNull
    private String productName;
    @NotNull
    private Category category;
    @NotNull
    private long price;
    @NotNull
    private int quantity;
}

