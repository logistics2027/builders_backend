package com.hltech.productservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hltech.productservice.Product;

import java.util.List;
import java.util.UUID;

public interface ProductRepository
        extends JpaRepository<Product, UUID> {

    boolean existsBySku(String sku);

    List<Product> findByCategoryId(UUID categoryId);
}
