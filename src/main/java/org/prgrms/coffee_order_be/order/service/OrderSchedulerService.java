package org.prgrms.coffee_order_be.order.service;

import lombok.RequiredArgsConstructor;
import org.prgrms.coffee_order_be.order.domain.Order;
import org.prgrms.coffee_order_be.order.domain.OrderStatus;
import org.prgrms.coffee_order_be.order.respository.OrderRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderSchedulerService {

    private final OrderRepository orderRepository;

    @Scheduled(cron = "0 0 14 * * *") // 매일 오후 2시에 실행
    public void updateOrderStatusToDeliveryStart() {
        LocalDateTime previousDayAt2PM = LocalDateTime.now().minusDays(1).withHour(14).withMinute(0).withSecond(0);
        LocalDateTime todayAt2PM = LocalDateTime.now().withHour(14).withMinute(0).withSecond(0);

        List<Order> orders = orderRepository.findByOrderStatusAndCreatedAtBetween(OrderStatus.ORDER_COMPLETE, previousDayAt2PM, todayAt2PM);

        for (Order order : orders) {
            order.updateOrderStatus(OrderStatus.DELIVERY_START);
        }
    }
}
