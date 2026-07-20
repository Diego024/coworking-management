package com.company.coworking.management.service;

import com.company.coworking.management.dto.request.CreateSpaceRequest;
import com.company.coworking.management.dto.request.EditSpaceRequest;
import com.company.coworking.management.dto.response.SpaceResponse;

import java.util.List;

public interface SpaceService {

    SpaceResponse createSpace(CreateSpaceRequest createSpaceRequest);

    List<SpaceResponse> getAllSpaces();

    SpaceResponse getSpaceById(Long id);

    SpaceResponse updateSpace(Long id, EditSpaceRequest request);

    void deleteSpace(Long id);
}
