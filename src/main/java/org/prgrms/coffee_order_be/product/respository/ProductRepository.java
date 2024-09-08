package org.prgrms.coffee_order_be.product.respository;

import org.prgrms.coffee_order_be.product.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    Page<Product> findAllByProductNameContaining(String productName, Pageable pageable);
}
