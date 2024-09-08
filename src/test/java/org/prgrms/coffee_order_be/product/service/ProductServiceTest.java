package org.prgrms.coffee_order_be.product.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgrms.coffee_order_be.product.domain.Category;
import org.prgrms.coffee_order_be.product.model.ProductDTO;
import org.prgrms.coffee_order_be.product.model.request.ProductRequestDTO;
import org.prgrms.coffee_order_be.product.model.request.UpdateProductDTO;
import org.prgrms.coffee_order_be.product.model.response.ProductResponseDTO;
import org.prgrms.coffee_order_be.product.respository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    private ProductResponseDTO productDTO1;
    private ProductResponseDTO productDTO2;
    private ProductResponseDTO productDTO3;

    @BeforeEach
    void setUp() {
        ProductRequestDTO requestDTO1 = createProductDTO("Test Product A", "믹스커피", 1000L, "taste good");
        ProductRequestDTO requestDTO2 = createProductDTO("Test Product B", "믹스커피", 2000L, "taste good");
        ProductRequestDTO requestDTO3 = createProductDTO("Test Product C", "믹스커피", 3000L, "taste good");

        productDTO1 = productService.createProduct(requestDTO1);
        productDTO2 = productService.createProduct(requestDTO2);
        productDTO3 = productService.createProduct(requestDTO3);
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("ID로 Product 조회 테스트")
    void findById() {
        ProductResponseDTO foundProductDTO = productService.getProduct(productDTO1.getProductId());

        assertProductEquals(productDTO1, foundProductDTO);
    }

    @Test
    @DisplayName("이름으로 Product 검색 테스트")
    void findByName() {
        Pageable pageable = PageRequest.of(0, 3);
        Page<ProductResponseDTO> productPage = productService.getProductsByName("Test Product", pageable);

        // 검증
        assertNotNull(productPage);
        assertEquals(3, productPage.getTotalElements());
        assertTrue(productPage.getContent().stream()
                .allMatch(p -> p.getProductName().contains("Test Product")));
    }

    @Test
    @DisplayName("Product 업데이트 테스트")
    void updateProduct() {
        // 먼저 저장된 ProductDTO 생성 및 저장
        ProductRequestDTO requestDTO = createProductDTO("Update Product", "믹스커피", 1000L, "description A");
        ProductResponseDTO savedProduct = productService.createProduct(requestDTO);

        // 업데이트할 정보 설정
        UpdateProductDTO updateDTO = UpdateProductDTO.builder()
                .price(1500L)
                .description("Updated Description")
                .build();

        // 업데이트 수행
        ProductResponseDTO updatedProduct = productService.updateProduct(savedProduct.getProductId(), updateDTO);

        // 업데이트된 정보 검증
        assertNotNull(updatedProduct);
        assertEquals(1500L, updatedProduct.getPrice());
        assertEquals("Updated Description", updatedProduct.getDescription());
        assertEquals(savedProduct.getProductName(), updatedProduct.getProductName()); // 이름은 그대로 유지됨
        assertEquals(savedProduct.getCategory(), updatedProduct.getCategory());       // 카테고리도 그대로 유지됨
    }

    private ProductRequestDTO createProductDTO(String productName, String category, long price, String description) {
        return ProductRequestDTO.builder()
                .productName(productName)
                .category(Category.valueOf(category))
                .price(price)
                .description(description)
                .build();
    }

    private void assertProductEquals(ProductResponseDTO expected, ProductResponseDTO actual) {
        assertNotNull(actual);
        assertEquals(expected.getProductName(), actual.getProductName());
        assertEquals(expected.getCategory(), actual.getCategory());
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(expected.getDescription(), actual.getDescription());
    }

}