package org.prgrms.coffee_order_be.order.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequestDTO {
    @NotNull
    private UUID productId;
    @NotNull
    private int quantity;
}
