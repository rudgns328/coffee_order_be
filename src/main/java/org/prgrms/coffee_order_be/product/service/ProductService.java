package org.prgrms.coffee_order_be.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.prgrms.coffee_order_be.common.exception.CustomException;
import org.prgrms.coffee_order_be.product.converter.ProductConverter;
import org.prgrms.coffee_order_be.product.domain.Product;
import org.prgrms.coffee_order_be.product.model.ProductDTO;
import org.prgrms.coffee_order_be.product.model.request.ProductRequestDTO;
import org.prgrms.coffee_order_be.product.model.request.UpdateProductDTO;
import org.prgrms.coffee_order_be.product.model.response.ProductResponseDTO;
import org.prgrms.coffee_order_be.product.respository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;
    private final ProductConverter productConverter;

    public ProductResponseDTO createProduct(ProductRequestDTO dto) {
        try {
            Product product = productConverter.convertToEntity(dto);
            Product savedProduct = productRepository.save(product);
            return productConverter.convertToDTO(savedProduct);
        } catch (Exception e) {
            logger.error("Error saving product", e);
            throw new CustomException("상품 등록 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Transactional(readOnly = true)
    public ProductResponseDTO getProduct(UUID productId) {
        return productRepository.findById(productId)
                .map(productConverter::convertToDTO)
                .orElseThrow(() -> new CustomException("존재하지 않는 상품 ID: " + productId, HttpStatus.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getProductsByName(String productName, Pageable pageable) {
        return productRepository.findAllByProductNameContaining(productName, pageable)
                .map(productConverter::convertToDTO);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productConverter::convertToDTO);
    }

    public ProductResponseDTO updateProduct(UUID productId, UpdateProductDTO dto) {
        Optional<Product> foundProduct = productRepository.findById(productId);

        if (foundProduct.isEmpty()) {
            throw new CustomException("존재하지 않는 상품 ID: " + productId, HttpStatus.NOT_FOUND);
        }

        Product product = foundProduct.get();

        if (dto.getPrice() <= 0) {
            throw new CustomException("가격은 0보다 커야 합니다.", HttpStatus.BAD_REQUEST);
        }

        product.updatePrice(dto.getPrice());

        if (dto.getDescription() != null) {
            product.updateDescription(dto.getDescription());
        }

        Product updatedProduct = productRepository.save(product);
        return productConverter.convertToDTO(updatedProduct);
    }

    public void deleteProduct(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException("존재하지 않는 상품 ID: " + productId, HttpStatus.NOT_FOUND));

        productRepository.delete(product);
    }
}
