package org.prgrms.coffee_order_be.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgrms.coffee_order_be.order.model.request.OrderCreateDTO;
import org.prgrms.coffee_order_be.order.model.request.OrderItemRequestDTO;
import org.prgrms.coffee_order_be.order.model.request.OrderUpdateDTO;
import org.prgrms.coffee_order_be.order.model.response.OrderResponseDTO;
import org.prgrms.coffee_order_be.order.respository.OrderRepository;
import org.prgrms.coffee_order_be.order.service.OrderService;
import org.prgrms.coffee_order_be.product.domain.Category;
import org.prgrms.coffee_order_be.product.domain.Product;
import org.prgrms.coffee_order_be.product.respository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    private OrderCreateDTO orderCreateDTO;


    @BeforeEach
    void setUp() {
        Product product1 = productRepository.save(new Product(UUID.randomUUID(), "Product 1", Category.AMERICANO, 1000L, "Description 1"));
        Product product2 = productRepository.save(new Product(UUID.randomUUID(), "Product 2", Category.AMERICANO, 2000L, "Description 2"));

        OrderItemRequestDTO item1 = new OrderItemRequestDTO(product1.getProductId(), 2);
        OrderItemRequestDTO item2 = new OrderItemRequestDTO(product2.getProductId(), 3);

        orderCreateDTO = new OrderCreateDTO("test123@example.com", "123 Main St", "12345", Arrays.asList(item1, item2));
    }


    @Test
    @DisplayName("주문 생성 테스트")
    void createOrder() throws Exception {
        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId").isNotEmpty())
                .andDo(print())
                .andReturn();
    }


    @Test
    @DisplayName("이메일로 주문 리스트 조회 테스트")
    void getOrdersByEmail() throws Exception {
        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderCreateDTO)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/order/list/{email}", "test123@example.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderId").isNotEmpty())
                .andExpect(jsonPath("$[0].email").value("test123@example.com"))
                .andExpect(jsonPath("$[0].orderItems[0].productName").value("Product 1"))
                .andDo(print());
    }


    @Test
    @DisplayName("주문 단건 조회 테스트")
    void getOrderById() throws Exception {
        String result = mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderCreateDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        OrderResponseDTO createdOrder = mapper.readValue(result, OrderResponseDTO.class);
        UUID createdOrderId = createdOrder.getOrderId();

        mockMvc.perform(get("/order/{orderId}", createdOrderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(createdOrderId.toString()))
                .andExpect(jsonPath("$.email").value("test123@example.com"))
                .andExpect(jsonPath("$.orderItems[0].productName").value("Product 1"))
                .andDo(print());
    }


    @Test
    @DisplayName("주문 수정 테스트 - ORDER_COMPLETE 상태일 때만 수정 가능")
    void updateOrder() throws Exception {
        String result = mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderCreateDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        OrderResponseDTO createdOrder = objectMapper.readValue(result, OrderResponseDTO.class);
        UUID createdOrderId = createdOrder.getOrderId();

        OrderUpdateDTO updateDTO = new OrderUpdateDTO("456 New Street", "67890");

        mockMvc.perform(patch("/order/{orderId}", createdOrderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("456 New Street"))
                .andExpect(jsonPath("$.postcode").value("67890"))
                .andDo(print());

        OrderResponseDTO updatedOrder = orderService.getOrderById(createdOrderId);
        assertEquals("456 New Street", updatedOrder.getAddress());
        assertEquals("67890", updatedOrder.getPostcode());
    }


    @Test
    @DisplayName("주문 삭제 성공 테스트 - ORDER_COMPLETE 상태일 때만 삭제 가능")
    void deleteOrder() throws Exception {
        String result = mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderCreateDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        OrderResponseDTO createdOrder = objectMapper.readValue(result, OrderResponseDTO.class);
        UUID createdOrderId = createdOrder.getOrderId();

        mockMvc.perform(delete("/order/{orderId}", createdOrderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());

        // 삭제 후 주문이 존재하지 않는지 확인
        mockMvc.perform(get("/order/{orderId}", createdOrderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }


    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
        productRepository.deleteAll();
    }

}