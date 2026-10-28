package com.hltech.builderservice.controller;

import com.hltech.builderservice.dto.DistributionResponse;
import com.hltech.builderservice.service.LayoutDistributionService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/layout-builder/distributions")
public class LayoutDistributionController {

    private final LayoutDistributionService service;

    public LayoutDistributionController(LayoutDistributionService service) {
        this.service = service;
    }

    // Publish: copy the layout table row into the distribution table
    @PostMapping("/{layoutId}/publish")
    @ResponseStatus(HttpStatus.CREATED)
    public DistributionResponse publish(@PathVariable Long layoutId) {
        return service.publish(layoutId);
    }

    // Storefront: active published layouts, ordered
    @GetMapping("/storefront")
    public List<DistributionResponse> storefront() {
        return service.getStorefront();
    }

    // Admin: published copy of one layout
    @GetMapping("/{layoutId}")
    public DistributionResponse get(@PathVariable Long layoutId) {
        return service.getByLayoutId(layoutId);
    }

    // Unpublish
    @DeleteMapping("/{layoutId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long layoutId) {
        service.deleteByLayoutId(layoutId);
    }
}