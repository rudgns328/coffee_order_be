package org.prgrms.coffee_order_be.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.prgrms.coffee_order_be.product.model.ProductDTO;
import org.prgrms.coffee_order_be.product.model.request.ProductRequestDTO;
import org.prgrms.coffee_order_be.product.model.request.UpdateProductDTO;
import org.prgrms.coffee_order_be.product.model.response.ProductResponseDTO;
import org.prgrms.coffee_order_be.product.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody @Valid ProductRequestDTO req) {
        return ResponseEntity.ok(productService.createProduct(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable UUID id) {
        ProductResponseDTO resp = productService.getProduct(id);
        return ResponseEntity.ok(resp);
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> getProductsByName(@RequestParam String productName,
                                                              @RequestParam int page,
                                                              @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponseDTO> resp = productService.getProductsByName(productName, pageable);
        return ResponseEntity.ok(resp);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable UUID id, @RequestBody @Valid UpdateProductDTO updateProductDTO) {
        ProductResponseDTO resp = productService.updateProduct(id, updateProductDTO);
        return ResponseEntity.ok(resp);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build(); // 상태 코드 204 반환
    }
}
