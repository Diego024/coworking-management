package com.company.coworking.management.service.impl;

import com.company.coworking.management.dto.request.CreateSpaceRequest;
import com.company.coworking.management.dto.request.EditSpaceRequest;
import com.company.coworking.management.dto.response.SpaceResponse;
import com.company.coworking.management.entity.Space;
import com.company.coworking.management.exception.business.SpaceNotFoundException;
import com.company.coworking.management.repository.SpaceRepository;
import com.company.coworking.management.service.SpaceService;
import com.company.coworking.management.mapper.SpaceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpaceServiceImpl implements SpaceService {

    private final SpaceRepository spaceRepository;
    private final SpaceMapper spaceMapper;

    @Override
    @Transactional
    public SpaceResponse createSpace(CreateSpaceRequest createSpaceRequest) {
        Space space = spaceMapper.toEntity(createSpaceRequest);
        spaceRepository.save(space);
        return spaceMapper.toResponse(space);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpaceResponse> getAllSpaces() {
        List<Space> spaces = spaceRepository.findAll();
        return spaceMapper.toResponseList(spaces);
    }

    @Transactional(readOnly = true)
    @Override
    public SpaceResponse getSpaceById(Long id) {
        Space space = spaceRepository.findById(id)
                .orElseThrow(() -> new SpaceNotFoundException(id));
        return spaceMapper.toResponse(space);
    }

    @Transactional
    @Override
    public SpaceResponse updateSpace(Long id, EditSpaceRequest request) {
        Space space = spaceRepository.findById(id)
                .orElseThrow(() -> new SpaceNotFoundException(id));

        spaceMapper.updateEntityFromRequest(request, space);

        space = spaceRepository.save(space);
        return spaceMapper.toResponse(space);
    }

    @Transactional
    @Override
    public void deleteSpace(Long id) {
        if (!spaceRepository.existsById(id)) {
            throw new SpaceNotFoundException(id);
        }
        spaceRepository.deleteById(id);
    }
}
