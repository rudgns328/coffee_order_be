package org.prgrms.coffee_order_be.product.converter;

import org.prgrms.coffee_order_be.product.domain.Product;
import org.prgrms.coffee_order_be.product.model.request.ProductRequestDTO;
import org.prgrms.coffee_order_be.product.model.response.ProductResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter {

    // 입력용 DTO -> 엔티티 변환
    public Product convertToEntity(ProductRequestDTO dto) {
        return Product.builder()
                .productName(dto.getProductName())
                .category(dto.getCategory())
                .price(dto.getPrice())
                .description(dto.getDescription())
                .build();
    }

    // 엔티티 -> 출력용 DTO 변환
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
