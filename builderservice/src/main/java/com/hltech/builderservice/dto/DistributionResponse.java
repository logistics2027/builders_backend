package com.hltech.builderservice.dto;

import com.hltech.builderservice.entity.LayoutDistribution;
import tools.jackson.databind.JsonNode;

import java.time.LocalDateTime;

public record DistributionResponse(
        Long id,
        Long layoutId,
        String name,
        Integer position,
        Boolean isActive,
        JsonNode layout,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {

    public static DistributionResponse from(LayoutDistribution d) {
        return new DistributionResponse(d.getId(), d.getLayoutId(), d.getName(), d.getPosition(),
                d.getIsActive(), d.getLayout(), d.getCreatedAt(), d.getUpdatedAt());
    }
}