package com.hltech.builderservice.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

// layout IDs in the new order, first = position 0
public record LayoutReorderRequest(@NotEmpty List<Long> layoutIds) {
}