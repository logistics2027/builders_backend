package com.hltech.builderservice.dto;

import com.hltech.builderservice.entity.Layout;
import tools.jackson.databind.JsonNode;

import java.time.LocalDateTime;

public record LayoutResponse(
        Long id,
        String name,
        Integer position,
        Boolean isActive,
        JsonNode layout,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {

    public static LayoutResponse from(Layout l) {
        return new LayoutResponse(l.getId(), l.getName(), l.getPosition(), l.getIsActive(),
                l.getLayout(), l.getCreatedAt(), l.getUpdatedAt());
    }
}