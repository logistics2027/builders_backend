package com.hltech.builderservice.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.time.LocalDateTime;

@Entity
@Table(name = "layout")
public class Layout {

    private static final JsonMapper MAPPER = JsonMapper.builder().build();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false)
    private Integer position = 0;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    // everything from the builder: colors, font size, drag/drop items, etc.
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "layout_json", columnDefinition = "jsonb", nullable = false)
    private String layoutJson = "{}";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
        if (isActive == null) isActive = true;
        if (position == null) position = 0;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Layout() {}

    public JsonNode getLayout() {
        try {
            return MAPPER.readTree(layoutJson);
        } catch (RuntimeException e) {
            return MAPPER.createObjectNode();
        }
    }

    public void setLayout(JsonNode layout) {
        this.layoutJson = MAPPER.writeValueAsString(layout);
    }

    public String getLayoutJson() { return layoutJson; }
    public void setLayoutJson(String layoutJson) { this.layoutJson = layoutJson; }

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}