package com.hltech.productservice;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

public record ProductRequest(

        @NotBlank @Size(max = 200) String name,

        @NotBlank @Size(max = 80) String sku,

        @Size(max = 2000) String description,

        @NotNull @DecimalMin(value = "0.01") @Digits(integer = 10, fraction = 2) BigDecimal price,

        @NotNull UUID categoryId,

        @PositiveOrZero int stockQuantity

) {
}
