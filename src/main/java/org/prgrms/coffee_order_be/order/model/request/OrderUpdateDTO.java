package org.prgrms.coffee_order_be.order.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderUpdateDTO {
    @NotNull
    private String address;
    @NotNull
    private String postcode;
}
