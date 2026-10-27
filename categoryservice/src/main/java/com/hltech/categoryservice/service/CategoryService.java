package com.hltech.categoryservice.service;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.hltech.categoryservice.Category;
import com.hltech.categoryservice.CategoryRequest;
import com.hltech.categoryservice.repo.CategoryRepository;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public List<Category> getAll() {
        return repository.findAll();
    }

    public Category getById(UUID id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Category not found"));
    }

    @Transactional
    public Category create(CategoryRequest request) {
        String name = request.name().trim();
        String slug = request.slug().trim().toLowerCase();

        if (repository.existsByNameIgnoreCase(name)) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT, "Category name already exists");
        }

        if (repository.existsBySlug(slug)) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT, "Category slug already exists");
        }

        return repository.save(new Category(
            name, slug, request.description()
        ));
    }

    @Transactional
    public Category update(UUID id, CategoryRequest request) {
        Category category = getById(id);
        String name = request.name().trim();
        String slug = request.slug().trim().toLowerCase();

        boolean duplicateName = repository.existsByNameIgnoreCase(name)
            && !category.getName().equalsIgnoreCase(name);

        boolean duplicateSlug = repository.existsBySlug(slug)
            && !category.getSlug().equals(slug);

        if (duplicateName || duplicateSlug) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT, "Category name or slug already exists");
        }

        category.update(name, slug, request.description());
        return repository.save(category);
    }

    @Transactional
    public void delete(UUID id) {
        Category category = getById(id);
        repository.delete(category);
    }
}
