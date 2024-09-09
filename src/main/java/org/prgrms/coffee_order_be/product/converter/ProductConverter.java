package org.prgrms.coffee_order_be.product.converter;

import org.prgrms.coffee_order_be.product.domain.Product;
import org.prgrms.coffee_order_be.product.model.request.ProductRequestDTO;
import org.prgrms.coffee_order_be.product.model.response.ProductResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter {

    public Product convertToEntity(ProductRequestDTO dto) {
        return Product.builder()
                .productName(dto.getProductName())
                .category(dto.getCategory())
                .price(dto.getPrice())
                .description(dto.getDescription())
                .build();
    }

    public ProductResponseDTO convertToDTO(Product product) {
        return ProductResponseDTO.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .category(product.getCategory())
                .price(product.getPrice())
                .description(product.getDescription())
                .build();
    }
}
