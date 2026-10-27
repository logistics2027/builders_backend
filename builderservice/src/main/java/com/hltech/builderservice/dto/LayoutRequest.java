package com.hltech.builderservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import tools.jackson.databind.JsonNode;

public record LayoutRequest(

    @NotBlank
    @Size(max = 255)
    String name,

    @NotBlank
    @Size(max = 50)
    String layoutType,

    JsonNode settings,

    Boolean isActive
) {
}