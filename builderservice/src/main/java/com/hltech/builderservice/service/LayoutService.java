package com.hltech.builderservice.service;

import com.hltech.builderservice.dto.LayoutProductRequest;
import com.hltech.builderservice.dto.LayoutRequest;
import com.hltech.builderservice.entity.Layout;
import com.hltech.builderservice.entity.LayoutProduct;
import com.hltech.builderservice.repo.LayoutProductRepository;
import com.hltech.builderservice.repo.LayoutRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class LayoutService {

    private final LayoutRepository layoutRepository;
    private final LayoutProductRepository productRepository;

    public LayoutService(
            LayoutRepository layoutRepository,
            LayoutProductRepository productRepository) {
        this.layoutRepository = layoutRepository;
        this.productRepository = productRepository;
    }

    // Create layout
    public Layout createLayout(LayoutRequest request) {
        Layout layout = new Layout();

        layout.setName(request.name());
        layout.setLayoutType(request.layoutType());

        // Safeguard: null / empty -> {}, data -> kept as is
        layout.setSettings(Layout.normalizeSettings(request.settings()));

        layout.setIsActive(
            request.isActive() == null ? true : request.isActive()
        );

        return layoutRepository.save(layout);
    }

    // Get all layouts
    @Transactional(readOnly = true)
    public List<Layout> getAllLayouts() {
        return layoutRepository.findAll();
    }

    // Get layout by ID
    @Transactional(readOnly = true)
    public Layout getLayoutById(Long id) {
        return findLayout(id);
    }

    // Update layout
    public Layout updateLayout(
            Long id,
            LayoutRequest request) {

        Layout layout = findLayout(id);

        if (request.name() != null) {
            layout.setName(request.name());
        }

        if (request.layoutType() != null) {
            layout.setLayoutType(request.layoutType());
        }

        // Field omitted -> keep existing settings.
        // Field sent as {} -> clears settings to empty object.
        // Field sent with data -> replaces settings.
        if (request.settings() != null) {
            layout.setSettings(Layout.normalizeSettings(request.settings()));
        }

        if (request.isActive() != null) {
            layout.setIsActive(request.isActive());
        }

        return layoutRepository.save(layout);
    }

    // Delete layout and its assigned products
    public void deleteLayout(Long id) {
        Layout layout = findLayout(id);
        layoutRepository.delete(layout);
    }

    // Add product to layout
    public LayoutProduct addProduct(
            Long layoutId,
            LayoutProductRequest request) {

        Layout layout = findLayout(layoutId);

        LayoutProduct product = new LayoutProduct();
        product.setLayout(layout);
        product.setProductId(request.productId());
        product.setPosition(request.position());
        product.setCustomTitle(request.customTitle());
        product.setCustomBadge(request.customBadge());

        return productRepository.save(product);
    }

    // Get products assigned to a layout
    @Transactional(readOnly = true)
    public List<LayoutProduct> getProducts(Long layoutId) {
        findLayout(layoutId);

        return productRepository
                .findByLayoutIdOrderByPositionAsc(layoutId);
    }

    // Update product assignment
    public LayoutProduct updateProduct(
            Long layoutId,
            Long productMappingId,
            LayoutProductRequest request) {

        findLayout(layoutId);

        LayoutProduct product = findProductInLayout(layoutId, productMappingId);

        if (request.productId() != null) {
            if (request.productId().isBlank()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Product ID cannot be blank"
                );
            }
            product.setProductId(request.productId());
        }

        if (request.position() != null) {
            product.setPosition(request.position());
        }

        if (request.customTitle() != null) {
            product.setCustomTitle(request.customTitle());
        }

        if (request.customBadge() != null) {
            product.setCustomBadge(request.customBadge());
        }

        return productRepository.save(product);
    }

    // Remove a product assignment without deleting the actual product
    public void removeProduct(Long layoutId, Long productMappingId) {
        findLayout(layoutId);

        LayoutProduct product = findProductInLayout(layoutId, productMappingId);

        productRepository.delete(product);
    }

    private LayoutProduct findProductInLayout(Long layoutId, Long productMappingId) {
        return productRepository.findById(productMappingId)
                .filter(p -> p.getLayout().getId().equals(layoutId))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product assignment not found in this layout"
                ));
    }

    private Layout findLayout(Long id) {
        return layoutRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Layout not found with ID: " + id
                ));
    }
}