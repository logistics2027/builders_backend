package com.hltech.productservice.controller;

import com.hltech.productservice.Product;
import com.hltech.productservice.ProductRequest;
import com.hltech.productservice.response.CategoryResponse;
import com.hltech.productservice.service.ProductService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public List<Product> getAll() {
        return service.getAll();
    }

    @GetMapping("/categories")
    public List<CategoryResponse> getCategories() {
        return service.getCategories();
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @GetMapping("/category/{categoryId}")
    public List<Product> getByCategory(
            @PathVariable UUID categoryId) {
        return service.getByCategory(categoryId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(
            @Valid @RequestBody ProductRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public Product update(
            @PathVariable UUID id,
            @Valid @RequestBody ProductRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}

