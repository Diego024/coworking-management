package com.company.coworking.management.service;

import com.company.coworking.management.dto.request.CreateSpaceRequest;
import com.company.coworking.management.dto.request.EditSpaceRequest;
import com.company.coworking.management.dto.response.SpaceResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SpaceService {
    @Transactional
    SpaceResponse createSpace(CreateSpaceRequest createSpaceRequest);

    @Transactional(readOnly = true)
    List<SpaceResponse> getAllSpaces();

    @Transactional(readOnly = true)
    SpaceResponse getSpaceById(Long id);

    @Transactional
    SpaceResponse updateSpace(Long id, EditSpaceRequest request);

    @Transactional
    void deleteSpace(Long id);
}
