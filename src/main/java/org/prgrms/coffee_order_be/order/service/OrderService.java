package org.prgrms.coffee_order_be.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.prgrms.coffee_order_be.common.exception.CustomException;
import org.prgrms.coffee_order_be.order.converter.OrderConverter;
import org.prgrms.coffee_order_be.order.domain.Order;
import org.prgrms.coffee_order_be.order.domain.OrderItem;
import org.prgrms.coffee_order_be.order.domain.OrderStatus;
import org.prgrms.coffee_order_be.order.model.request.OrderCreateDTO;
import org.prgrms.coffee_order_be.order.model.request.OrderUpdateDTO;
import org.prgrms.coffee_order_be.order.model.response.OrderResponseDTO;
import org.prgrms.coffee_order_be.order.respository.OrderItemRepository;
import org.prgrms.coffee_order_be.order.respository.OrderRepository;
import org.prgrms.coffee_order_be.product.domain.Product;
import org.prgrms.coffee_order_be.product.respository.ProductRepository;
import org.prgrms.coffee_order_be.order.model.request.OrderItemRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final OrderConverter orderConverter;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    public OrderResponseDTO createOrder(OrderCreateDTO req) {
        Order order = orderConverter.convertToEntity(req);
        orderRepository.save(order);

        List<Product> products = productRepository.findAllById(
                req.getOrderItems().stream()
                        .map(orderItem -> orderItem.getProductId())
                        .collect(Collectors.toList())
        );

        if (products.isEmpty()) {
            throw new CustomException("해당 productId를 가진 product가 없습니다.", HttpStatus.NOT_FOUND);
        }

        List<OrderItem> orderItems = orderConverter.convertToOrderItems(order, req.getOrderItems(), products);
        orderItemRepository.saveAll(orderItems);

        return orderConverter.convertToResponse(order, orderItems);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getOrdersByEmail(String email) {
        List<Order> orders = orderRepository.findByEmail(email);

        if (orders.isEmpty()) {
            throw new CustomException("해당 이메일로 주문한 건이 없습니다: " + email, HttpStatus.NOT_FOUND);
        }

        return orders.stream()
                .map(order -> orderConverter.convertToResponse(order, order.getOrderItems()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderResponseDTO getOrderById(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("존재하지 않는 주문 ID: " + orderId, HttpStatus.NOT_FOUND));

        List<OrderItem> orderItems = order.getOrderItems();
        return orderConverter.convertToResponse(order, orderItems);
    }

    public OrderResponseDTO updateOrder(UUID orderId, OrderUpdateDTO updateDTO) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("존재하지 않는 주문 ID: " + orderId, HttpStatus.NOT_FOUND));

        if (order.getOrderStatus() != OrderStatus.ORDER_COMPLETE) {
            throw new CustomException("배송이 시작된 이후에는 주문을 수정할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }

        order.updateAddress(updateDTO.getAddress(), updateDTO.getPostcode());

        return orderConverter.convertToResponse(order, order.getOrderItems());
    }

    public void deleteOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("존재하지 않는 주문 ID: " + orderId, HttpStatus.NOT_FOUND));

        if (order.getOrderStatus() != OrderStatus.ORDER_COMPLETE) {
            throw new CustomException("배송이 시작된 이후에는 주문을 삭제할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }

        orderRepository.delete(order);
    }
}
