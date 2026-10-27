package com.hltech.builderservice.controller;

import com.hltech.builderservice.dto.LayoutProductRequest;
import com.hltech.builderservice.dto.LayoutRequest;
import com.hltech.builderservice.entity.Layout;
import com.hltech.builderservice.entity.LayoutProduct;
import com.hltech.builderservice.service.LayoutService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/layouts")
public class LayoutController {

    private final LayoutService layoutService;

    public LayoutController(LayoutService layoutService) {
        this.layoutService = layoutService;
    }

    // Create layout
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Layout createLayout(
            @Valid @RequestBody LayoutRequest request) {
        return layoutService.createLayout(request);
    }

    // Get all layouts
    @GetMapping
    public List<Layout> getAllLayouts() {
        return layoutService.getAllLayouts();
    }

    // Get layout by ID
    @GetMapping("/{id}")
    public Layout getLayoutById(@PathVariable Long id) {
        return layoutService.getLayoutById(id);
    }

    // Update layout
    @PutMapping("/{id}")
    public Layout updateLayout(
            @PathVariable Long id,
            @Valid @RequestBody LayoutRequest request) {
        return layoutService.updateLayout(id, request);
    }

    // Delete layout
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLayout(@PathVariable Long id) {
        layoutService.deleteLayout(id);
    }

    // Add product to layout
    @PostMapping("/{layoutId}/products")
    @ResponseStatus(HttpStatus.CREATED)
    public LayoutProduct addProduct(
            @PathVariable Long layoutId,
            @Valid @RequestBody LayoutProductRequest request) {
        return layoutService.addProduct(layoutId, request);
    }

    // Get products in layout
    @GetMapping("/{layoutId}/products")
    public List<LayoutProduct> getProducts(
            @PathVariable Long layoutId) {
        return layoutService.getProducts(layoutId);
    }

    // Update product assignment
    @PutMapping("/{layoutId}/products/{productMappingId}")
    public LayoutProduct updateProduct(
            @PathVariable Long layoutId,
            @PathVariable Long productMappingId,
            @Valid @RequestBody LayoutProductRequest request) {
        return layoutService.updateProduct(
                layoutId, productMappingId, request);
    }

    // Remove product assignment
    @DeleteMapping("/{layoutId}/products/{productMappingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeProduct(
            @PathVariable Long layoutId,
            @PathVariable Long productMappingId) {
        layoutService.removeProduct(layoutId, productMappingId);
    }
}
