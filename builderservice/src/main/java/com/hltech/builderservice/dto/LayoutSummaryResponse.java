package com.hltech.builderservice.dto;

import com.hltech.builderservice.entity.Layout;

public record LayoutSummaryResponse(
        Long id,
        String name,
        Integer position,
        Boolean isActive) {

    public static LayoutSummaryResponse from(Layout l) {
        return new LayoutSummaryResponse(l.getId(), l.getName(), l.getPosition(), l.getIsActive());
    }
}