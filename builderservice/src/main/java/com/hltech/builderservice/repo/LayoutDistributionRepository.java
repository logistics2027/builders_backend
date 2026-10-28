package com.hltech.builderservice.repo;

import com.hltech.builderservice.entity.LayoutDistribution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LayoutDistributionRepository extends JpaRepository<LayoutDistribution, Long> {

    Optional<LayoutDistribution> findByLayoutId(Long layoutId);

    List<LayoutDistribution> findByIsActiveTrueOrderByPositionAsc();

    void deleteByLayoutId(Long layoutId);
}