package com.hltech.builderservice.service;

import com.hltech.builderservice.dto.DistributionResponse;
import com.hltech.builderservice.entity.Layout;
import com.hltech.builderservice.entity.LayoutDistribution;
import com.hltech.builderservice.repo.LayoutDistributionRepository;
import com.hltech.builderservice.repo.LayoutRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class LayoutDistributionService {

    private final LayoutDistributionRepository distributionRepository;
    private final LayoutRepository layoutRepository;

    public LayoutDistributionService(LayoutDistributionRepository distributionRepository,
                                     LayoutRepository layoutRepository) {
        this.distributionRepository = distributionRepository;
        this.layoutRepository = layoutRepository;
    }

    /** Publish: copies the layout row (name, position, active, JSON) into the distribution table. */
    public DistributionResponse publish(Long layoutId) {
        Layout l = layoutRepository.findById(layoutId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Layout not found with ID: " + layoutId));

        LayoutDistribution d = distributionRepository.findByLayoutId(layoutId)
                .orElseGet(() -> {
                    LayoutDistribution created = new LayoutDistribution();
                    created.setLayoutId(layoutId);
                    return created;
                });

        d.setName(l.getName());
        d.setPosition(l.getPosition());
        d.setIsActive(l.getIsActive());
        d.setLayoutJson(l.getLayoutJson());

        return DistributionResponse.from(distributionRepository.save(d));
    }

    /** Admin: open the published copy of one layout. */
    @Transactional(readOnly = true)
    public DistributionResponse getByLayoutId(Long layoutId) {
        return DistributionResponse.from(distributionRepository.findByLayoutId(layoutId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Layout is not published: " + layoutId)));
    }

    /** Storefront: active published layouts in order. */
    @Transactional(readOnly = true)
    public List<DistributionResponse> getStorefront() {
        return distributionRepository.findByIsActiveTrueOrderByPositionAsc().stream()
                .map(DistributionResponse::from).toList();
    }

    public void deleteByLayoutId(Long layoutId) {
        distributionRepository.deleteByLayoutId(layoutId);
    }

    /** Called after drag/drop reorder so published copies keep the same order. */
    void syncPositions(List<Layout> layouts) {
        Map<Long, Integer> positions = layouts.stream()
                .collect(Collectors.toMap(Layout::getId, Layout::getPosition));
        List<LayoutDistribution> all = distributionRepository.findAll();
        all.forEach(d -> {
            Integer p = positions.get(d.getLayoutId());
            if (p != null) d.setPosition(p);
        });
        distributionRepository.saveAll(all);
    }
}