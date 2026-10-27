package com.hltech.productservice;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, unique = true, length = 80)
    private String sku;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "category_id", nullable = false)
    private UUID categoryId;

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    protected Product() {
    }

    public Product(
            String name,
            String sku,
            String description,
            BigDecimal price,
            UUID categoryId,
            int stockQuantity) {
        this.name = name;
        this.sku = sku;
        this.description = description;
        this.price = price;
        this.categoryId = categoryId;
        this.stockQuantity = stockQuantity;
    }

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getSku() { return sku; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public UUID getCategoryId() { return categoryId; }
    public int getStockQuantity() { return stockQuantity; }
    public boolean isActive() { return active; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public void update(
            String name,
            String sku,
            String description,
            BigDecimal price,
            UUID categoryId,
            int stockQuantity) {
        this.name = name;
        this.sku = sku;
        this.description = description;
        this.price = price;
        this.categoryId = categoryId;
        this.stockQuantity = stockQuantity;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
