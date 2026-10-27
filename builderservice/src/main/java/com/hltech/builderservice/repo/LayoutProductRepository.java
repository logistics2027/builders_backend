package com.hltech.builderservice.repo;

import com.hltech.builderservice.entity.LayoutProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LayoutProductRepository
        extends JpaRepository<LayoutProduct, Long> {

    List<LayoutProduct> findByLayoutIdOrderByPositionAsc(Long layoutId);

    List<LayoutProduct> findByLayoutId(Long layoutId);
}
