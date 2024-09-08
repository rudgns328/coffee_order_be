//package org.prgrms.coffee_order_be.orderItem.domain;
//
//import jakarta.persistence.*;
//import jakarta.validation.constraints.NotNull;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import org.prgrms.coffee_order_be.common.BaseEntity;
//import org.prgrms.coffee_order_be.order.domain.Order;
//import org.prgrms.coffee_order_be.product.domain.Product;
//
//@Getter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(name = "order_items")
//@Entity
//public class OrderItem extends BaseEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long seq;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "order_id", nullable = false)
//    private Order order;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "product_id", nullable = false)
//    private Product product;
//
//    @NotNull
//    @Column(length = 50)
//    private String category;
//
//    @NotNull
//    private long price;
//
//    @NotNull
//    private int quantity;
//
//
//}
