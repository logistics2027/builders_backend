package com.hltech.productservice.response;

import java.util.UUID;

public record CategoryResponse(
    UUID id,
    String name,
    String slug,
    String description,
    boolean active
) {}