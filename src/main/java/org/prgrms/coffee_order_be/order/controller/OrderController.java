package org.prgrms.coffee_order_be.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.prgrms.coffee_order_be.order.model.request.OrderCreateDTO;
import org.prgrms.coffee_order_be.order.model.request.OrderUpdateDTO;
import org.prgrms.coffee_order_be.order.model.response.OrderResponseDTO;
import org.prgrms.coffee_order_be.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody @Valid OrderCreateDTO orderCreateDTO) {
        OrderResponseDTO response = orderService.createOrder(orderCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/list/{email}")
    public ResponseEntity<List<OrderResponseDTO>> getOrderByEmail(@PathVariable String email) {
        List<OrderResponseDTO> orderList = orderService.getOrdersByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(orderList);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable UUID orderId) {
        OrderResponseDTO response = orderService.getOrderById(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> updateOrder(
            @PathVariable UUID orderId,
            @RequestBody @Valid OrderUpdateDTO orderUpdateDTO) {
        OrderResponseDTO updatedOrder = orderService.updateOrder(orderId, orderUpdateDTO);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}
