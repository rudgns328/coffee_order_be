package org.prgrms.coffee_order_be.product.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.coffee_order_be.product.domain.Category;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {

    @NotNull
    private String productName;

    @NotNull
    private Category category;

    @NotNull
    private long price;

    private String description;
}
