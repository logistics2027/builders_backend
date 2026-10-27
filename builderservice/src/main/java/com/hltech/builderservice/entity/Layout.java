package com.hltech.builderservice.entity;

import jakarta.persistence.*;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "layouts")
public class Layout {

    private static final JsonMapper MAPPER = JsonMapper.builder().build();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(name = "layout_type", nullable = false, length = 50)
    private String layoutType;

    // Stored as a JSON string so Hibernate needs no Jackson mapping.
    // Never null: defaults to {}
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "settings", columnDefinition = "jsonb", nullable = false)
    private String settingsJson = "{}";

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "layout", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position ASC")
    private List<LayoutProduct> products = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;

        if (isActive == null) {
            isActive = true;
        }
        if (settingsJson == null || settingsJson.isBlank()) {
            settingsJson = "{}";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        if (settingsJson == null || settingsJson.isBlank()) {
            settingsJson = "{}";
        }
    }

    /** Returns {} if the value is null, JSON null or missing; otherwise the data unchanged. */
    public static JsonNode normalizeSettings(JsonNode node) {
        if (node == null || node.isNull() || node.isMissingNode()) {
            return MAPPER.createObjectNode();
        }
        return node;
    }

    /** True if settings contains data (not just {}). */
    public boolean hasSettingsData() {
        return !getSettings().isEmpty();
    }

    public Layout() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(String layoutType) {
        this.layoutType = layoutType;
    }

    // API-facing getter: JSON string in DB -> real JSON in the response
    public JsonNode getSettings() {
        try {
            if (settingsJson == null || settingsJson.isBlank()) {
                return MAPPER.createObjectNode();
            }
            return normalizeSettings(MAPPER.readTree(settingsJson));
        } catch (RuntimeException e) {
            return MAPPER.createObjectNode();
        }
    }

    // API-facing setter: JsonNode from request -> JSON string for the DB
    public void setSettings(JsonNode settings) {
        this.settingsJson = MAPPER.writeValueAsString(normalizeSettings(settings));
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<LayoutProduct> getProducts() {
        return products;
    }
}