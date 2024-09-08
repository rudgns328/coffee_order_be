//package org.prgrms.coffee_order_be.order.domain;
//
//import jakarta.persistence.*;
//import jakarta.validation.constraints.NotNull;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import org.prgrms.coffee_order_be.common.BaseEntity;
//
//import java.util.UUID;
//
//@Getter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(name = "orders")
//@Entity
//public class Order extends BaseEntity {
//
//    @Id
//    private UUID orderId;
//
//    @NotNull
//    @Column(length = 50)
//    private String email;
//
//    @NotNull
//    @Column(length = 200)
//    private String address;
//
//    @NotNull
//    @Column(length = 200)
//    private String postcode;
//
//    @NotNull
//    @Column(length = 50)
//    @Enumerated(EnumType.STRING)
//    private OrderStatus orderStatus;
//}
