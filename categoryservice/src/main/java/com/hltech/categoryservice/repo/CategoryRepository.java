package com.hltech.categoryservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hltech.categoryservice.Category;

import java.util.UUID;

public interface CategoryRepository
        extends JpaRepository<Category, UUID> {

    boolean existsByNameIgnoreCase(String name);

    boolean existsBySlug(String slug);
}