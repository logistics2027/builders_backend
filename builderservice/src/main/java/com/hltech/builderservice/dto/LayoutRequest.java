package com.hltech.builderservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import tools.jackson.databind.JsonNode;

public record LayoutRequest(
        @NotBlank @Size(max = 255) String name,
        Boolean isActive,
        @NotNull JsonNode layout) {   // full builder JSON: colors, font size, items...
}