package com.hltech.builderservice.service;

import com.hltech.builderservice.dto.LayoutRequest;
import com.hltech.builderservice.dto.LayoutResponse;
import com.hltech.builderservice.dto.LayoutSummaryResponse;
import com.hltech.builderservice.entity.Layout;
import com.hltech.builderservice.repo.LayoutRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.JsonNode;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class LayoutService {

    private final LayoutRepository layoutRepository;
    private final LayoutDistributionService distributionService;

    public LayoutService(LayoutRepository layoutRepository,
                         LayoutDistributionService distributionService) {
        this.layoutRepository = layoutRepository;
        this.distributionService = distributionService;
    }

    /** First Save click: new layout goes to the end of the order. */
    public LayoutResponse create(LayoutRequest r) {
        validate(r.layout());
        Layout l = new Layout();
        l.setName(r.name());
        l.setIsActive(r.isActive() == null ? true : r.isActive());
        l.setLayout(r.layout());
        l.setPosition(layoutRepository.findMaxPosition() + 1);
        return LayoutResponse.from(layoutRepository.save(l));
    }

    /** Every next Save click: everything is stored again in the layout table. */
    public LayoutResponse update(Long id, LayoutRequest r) {
        validate(r.layout());
        Layout l = getEntity(id);
        l.setName(r.name());
        if (r.isActive() != null) l.setIsActive(r.isActive());
        l.setLayout(r.layout());
        return LayoutResponse.from(layoutRepository.save(l));
    }

    /** Open one layout with its full JSON. */
    @Transactional(readOnly = true)
    public LayoutResponse getById(Long id) {
        return LayoutResponse.from(getEntity(id));
    }

    /** Ordered list without JSON. */
    @Transactional(readOnly = true)
    public List<LayoutSummaryResponse> getAll() {
        return layoutRepository.findAllByOrderByPositionAsc().stream()
                .map(LayoutSummaryResponse::from).toList();
    }

    /** Deletes the layout and its published copy. */
    public void delete(Long id) {
        Layout l = getEntity(id);
        distributionService.deleteByLayoutId(id);
        layoutRepository.delete(l);
    }

    /** Drag and drop order. Must contain every layout exactly once. */
    public List<LayoutSummaryResponse> reorder(List<Long> layoutIds) {
        List<Layout> layouts = layoutRepository.findAll();
        Map<Long, Layout> byId = layouts.stream()
                .collect(Collectors.toMap(Layout::getId, Function.identity()));

        if (layoutIds.size() != new HashSet<>(layoutIds).size()
                || !byId.keySet().equals(new HashSet<>(layoutIds))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "layoutIds must contain every existing layout exactly once");
        }

        for (int i = 0; i < layoutIds.size(); i++) {
            byId.get(layoutIds.get(i)).setPosition(i);
        }
        layoutRepository.saveAll(layouts);
        distributionService.syncPositions(layouts); // keep storefront order in sync

        return layoutIds.stream().map(byId::get).map(LayoutSummaryResponse::from).toList();
    }

    private Layout getEntity(Long id) {
        return layoutRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Layout not found with ID: " + id));
    }

    private void validate(JsonNode layout) {
        if (layout == null || !layout.isObject()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "layout must be a JSON object");
        }
    }
}