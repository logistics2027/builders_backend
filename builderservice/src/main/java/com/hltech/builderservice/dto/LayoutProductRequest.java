package com.hltech.builderservice.dto;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LayoutProductRequest(

    @NotBlank
    @Size(max = 255)
    String productId,

    @NotNull
    Integer position,

    @Size(max = 255)
    String customTitle,

    @Size(max = 50)
    String customBadge
) {
}
