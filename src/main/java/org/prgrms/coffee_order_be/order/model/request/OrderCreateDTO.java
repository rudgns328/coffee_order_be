package org.prgrms.coffee_order_be.order.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateDTO {
    @NotNull
    private String email;
    @NotNull
    private String address;
    @NotNull
    private String postcode;
    @NotNull
    private List<OrderItemRequestDTO> orderItems;
}
