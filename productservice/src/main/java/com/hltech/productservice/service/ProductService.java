package com.hltech.productservice.service;

import com.hltech.productservice.Product;
import com.hltech.productservice.ProductRequest;
import com.hltech.productservice.client.CategoryClient;
import com.hltech.productservice.repo.ProductRepository;
import com.hltech.productservice.response.CategoryResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final CategoryClient categoryClient;

    public ProductService(
            ProductRepository repository,
            CategoryClient categoryClient) {
        this.repository = repository;
        this.categoryClient = categoryClient;
    }

    public List<Product> getAll() {
        return repository.findAll();
    }

    public Product getById(UUID id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Product not found"));
    }

    public List<Product> getByCategory(UUID categoryId) {
        return repository.findByCategoryId(categoryId);
    }

    // Fetch categories for the product creation form
    public List<CategoryResponse> getCategories() {
        return categoryClient.getAllCategories();
    }

    @Transactional
    public Product create(ProductRequest request) {
        // Ensure the selected category exists
        categoryClient.validateCategory(request.categoryId());

        String sku = request.sku().trim().toUpperCase();

        if (repository.existsBySku(sku)) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT, "SKU already exists");
        }

        return repository.save(new Product(
            request.name().trim(),
            sku,
            request.description(),
            request.price(),
            request.categoryId(),
            request.stockQuantity()
        ));
    }

    @Transactional
    public Product update(UUID id, ProductRequest request) {
        Product product = getById(id);

        // Validate category when changing/updating a product
        categoryClient.validateCategory(request.categoryId());

        String sku = request.sku().trim().toUpperCase();

        if (repository.existsBySku(sku)
                && !product.getSku().equalsIgnoreCase(sku)) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT, "SKU already exists");
        }

        product.update(
            request.name().trim(),
            sku,
            request.description(),
            request.price(),
            request.categoryId(),
            request.stockQuantity()
        );

        return repository.save(product);
    }

    @Transactional
    public void delete(UUID id) {
        Product product = getById(id);
        repository.delete(product);
    }
}

