package org.prgrms.coffee_order_be.order.converter;

import org.prgrms.coffee_order_be.order.domain.Order;
import org.prgrms.coffee_order_be.order.domain.OrderItem;
import org.prgrms.coffee_order_be.order.model.request.OrderCreateDTO;
import org.prgrms.coffee_order_be.order.model.request.OrderItemRequestDTO;
import org.prgrms.coffee_order_be.order.model.response.OrderItemResponseDTO;
import org.prgrms.coffee_order_be.order.model.response.OrderResponseDTO;
import org.prgrms.coffee_order_be.product.domain.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderConverter {

    public Order convertToEntity(OrderCreateDTO request) {
        return Order.builder()
                .email(request.getEmail())
                .address(request.getAddress())
                .postcode(request.getPostcode())
                .build();
    }

    public List<OrderItem> convertToOrderItems(Order order, List<OrderItemRequestDTO> orderItemRequests, List<Product> products) {
        return orderItemRequests.stream()
                .map(orderItemRequest -> {
                    Product product = products.stream()
                            .filter(p -> p.getProductId().equals(orderItemRequest.getProductId()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품 ID: " + orderItemRequest.getProductId()));

                    return OrderItem.builder()
                            .order(order)
                            .product(product)
                            .category(product.getCategory())
                            .price(product.getPrice() * orderItemRequest.getQuantity()) // 가격은 수량과 곱하여 계산
                            .quantity(orderItemRequest.getQuantity())
                            .build();
                }).collect(Collectors.toList());
    }

    public OrderResponseDTO convertToResponse(Order order, List<OrderItem> orderItems) {
        List<OrderItemResponseDTO> orderItemResponses = orderItems.stream()
                .map(orderItem -> new OrderItemResponseDTO(
                        orderItem.getProduct().getProductId(),
                        orderItem.getProduct().getProductName(),
                        orderItem.getCategory(),
                        orderItem.getPrice(),
                        orderItem.getQuantity()
                ))
                .collect(Collectors.toList());

        return OrderResponseDTO.builder()
                .orderId(order.getOrderId())
                .email(order.getEmail())
                .address(order.getAddress())
                .postcode(order.getPostcode())
                .orderStatus(order.getOrderStatus())
                .orderItems(orderItemResponses)
                .build();
    }

}
