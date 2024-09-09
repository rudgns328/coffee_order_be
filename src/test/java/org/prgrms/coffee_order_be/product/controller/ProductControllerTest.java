package org.prgrms.coffee_order_be.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgrms.coffee_order_be.product.domain.Category;
import org.prgrms.coffee_order_be.product.model.request.ProductRequestDTO;
import org.prgrms.coffee_order_be.product.model.response.ProductResponseDTO;
import org.prgrms.coffee_order_be.product.respository.ProductRepository;
import org.prgrms.coffee_order_be.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductService productService;


    @Test
    @DisplayName("Product 저장 테스트")
    void saveProduct() throws Exception {
        ProductRequestDTO dto = ProductRequestDTO.builder()
                .productName("Test Product")
                .category(Category.valueOf("AMERICANO"))
                .price(1000L)
                .description("Test Description")
                .build();

        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Test Product"))
                .andExpect(jsonPath("$.productId").isNotEmpty());
    }


    @Test
    @DisplayName("ID로 Product 조회 테스트")
    void getProductById() throws Exception {
        ProductRequestDTO dto = ProductRequestDTO.builder()
                .productName("Test Product")
                .category(Category.valueOf("AMERICANO"))
                .price(1000L)
                .description("Test Description")
                .build();

        ProductResponseDTO savedProduct = productService.createProduct(dto);

        mockMvc.perform(get("/product/{id}", savedProduct.getProductId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Test Product"))
                .andExpect(jsonPath("$.category").value("AMERICANO"))
                .andExpect(jsonPath("$.description").value("Test Description"));
    }


    @Test
    @DisplayName("Product 업데이트 테스트")
    void updateProduct() throws Exception {
        ProductRequestDTO dto = ProductRequestDTO.builder()
                .productName("Test Product")
                .category(Category.valueOf("AMERICANO"))
                .price(1000L)
                .description("Original Description")
                .build();

        ProductResponseDTO savedProduct = productService.createProduct(dto);

        String updateJson = """
                {
                    "price": 1500,
                    "description": "Update Description"
                }
                """;

        mockMvc.perform(put("/product/{id}", savedProduct.getProductId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Test Product"))
                .andExpect(jsonPath("$.category").value("AMERICANO"))
                .andExpect(jsonPath("$.description").value("Update Description"))
                .andExpect(jsonPath("$.price").value(1500));
    }


    @Test
    @DisplayName("Product 삭제 테스트")
    void deleteProduct() throws Exception {
        ProductRequestDTO dto = ProductRequestDTO.builder()
                .productName("Test Product")
                .category(Category.valueOf("AMERICANO"))
                .price(1000L)
                .description("Test Description")
                .build();

        ProductResponseDTO savedProduct = productService.createProduct(dto);

        mockMvc.perform(delete("/product/{id}", savedProduct.getProductId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // 삭제 후 조회 시도 -> 존재하지 않음
        mockMvc.perform(get("/product/{id}", savedProduct.getProductId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}