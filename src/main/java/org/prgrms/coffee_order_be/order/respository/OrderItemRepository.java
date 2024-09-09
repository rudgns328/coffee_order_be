package org.prgrms.coffee_order_be.order.respository;

import org.prgrms.coffee_order_be.order.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
