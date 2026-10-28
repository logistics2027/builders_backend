package com.hltech.builderservice.controller;

import com.hltech.builderservice.dto.LayoutReorderRequest;
import com.hltech.builderservice.dto.LayoutRequest;
import com.hltech.builderservice.dto.LayoutResponse;
import com.hltech.builderservice.dto.LayoutSummaryResponse;
import com.hltech.builderservice.service.LayoutService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/layout-builder/layouts")
public class LayoutController {

    private final LayoutService service;

    public LayoutController(LayoutService service) {
        this.service = service;
    }

    // First Save click
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LayoutResponse create(@Valid @RequestBody LayoutRequest request) {
        return service.create(request);
    }

    // Every next Save click (colors, font size, drag/drop, new items...)
    @PutMapping("/{id}")
    public LayoutResponse update(@PathVariable Long id, @Valid @RequestBody LayoutRequest request) {
        return service.update(id, request);
    }

    // Open one layout with full JSON
    @GetMapping("/{id}")
    public LayoutResponse get(@PathVariable Long id) {
        return service.getById(id);
    }

    // Ordered list, no JSON
    @GetMapping
    public List<LayoutSummaryResponse> getAll() {
        return service.getAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // Drag and drop "Save order"
    @PutMapping("/order")
    public List<LayoutSummaryResponse> reorder(@Valid @RequestBody LayoutReorderRequest request) {
        return service.reorder(request.layoutIds());
    }
}