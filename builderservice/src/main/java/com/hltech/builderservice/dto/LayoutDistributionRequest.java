package com.hltech.builderservice.dto;

import jakarta.validation.constraints.NotNull;
import tools.jackson.databind.JsonNode;

public record LayoutDistributionRequest(
        @NotNull JsonNode layout) {   // { layoutSettings, layoutContent }
}