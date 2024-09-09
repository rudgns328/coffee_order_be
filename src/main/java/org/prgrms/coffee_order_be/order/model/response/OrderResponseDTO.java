package org.prgrms.coffee_order_be.order.model.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.coffee_order_be.order.domain.OrderStatus;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    @NotNull
    private UUID orderId;
    @NotNull
    private String email;
    @NotNull
    private String address;
    @NotNull
    private String postcode;
    @NotNull
    private OrderStatus orderStatus;
    @NotNull
    private List<OrderItemResponseDTO> orderItems;
}
