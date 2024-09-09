package org.prgrms.coffee_order_be.order.respository;

import org.prgrms.coffee_order_be.order.domain.Order;
import org.prgrms.coffee_order_be.order.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByEmail(String email);
    List<Order> findByOrderStatusAndCreatedAtBetween(OrderStatus orderStatus, LocalDateTime startDate, LocalDateTime endDate);
}
