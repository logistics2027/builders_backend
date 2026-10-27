package com.hltech.builderservice.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "layout_products")
public class LayoutProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "layout_id", nullable = false)
    private Layout layout;

    @Column(name = "product_id", nullable = false, length = 255)
    private String productId;

    @Column(nullable = false)
    private Integer position = 0;

    @Column(name = "custom_title", length = 255)
    private String customTitle;

    @Column(name = "custom_badge", length = 50)
    private String customBadge;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();

        if (position == null) {
            position = 0;
        }
    }

    public LayoutProduct() {
    }

    public Long getId() {
        return id;
    }

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getCustomTitle() {
        return customTitle;
    }

    public void setCustomTitle(String customTitle) {
        this.customTitle = customTitle;
    }

    public String getCustomBadge() {
        return customBadge;
    }

    public void setCustomBadge(String customBadge) {
        this.customBadge = customBadge;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
