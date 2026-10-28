package com.hltech.builderservice.repo;

import com.hltech.builderservice.entity.Layout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LayoutRepository extends JpaRepository<Layout, Long> {

    List<Layout> findAllByOrderByPositionAsc();

    @Query("select coalesce(max(l.position), -1) from Layout l")
    int findMaxPosition();
}