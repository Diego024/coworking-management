package com.company.coworking.management.controller;

import com.company.coworking.management.dto.request.CreateSpaceRequest;
import com.company.coworking.management.dto.request.EditSpaceRequest;
import com.company.coworking.management.common.response.GeneralResponse;
import com.company.coworking.management.common.response.ResponseBuilder;
import com.company.coworking.management.dto.response.SpaceResponse;
import com.company.coworking.management.service.SpaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/space")
@RequiredArgsConstructor
public class SpaceController {

    private final SpaceService spaceService;

    @PostMapping
    public ResponseEntity<GeneralResponse> createSpace(@RequestBody @Valid CreateSpaceRequest createSpaceRequest) {
        SpaceResponse spaceResponse = spaceService.createSpace(createSpaceRequest);
        return ResponseBuilder.buildCreatedResponse(spaceResponse);
    }

    @GetMapping
    public ResponseEntity<GeneralResponse> getAllSpaces() {
        List<SpaceResponse> spaceResponseList = spaceService.getAllSpaces();
        return ResponseBuilder.buildSuccessResponse(spaceResponseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getSpaceById(@PathVariable Long id) {
        SpaceResponse spaceResponse = spaceService.getSpaceById(id);
        return ResponseBuilder.buildSuccessResponse(spaceResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GeneralResponse> updateSpace(@PathVariable Long id, @RequestBody @Valid EditSpaceRequest request) {
        SpaceResponse spaceResponse = spaceService.updateSpace(id, request);
        return ResponseBuilder.buildSuccessResponse(spaceResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse> deleteSpace(@PathVariable Long id) {
        spaceService.deleteSpace(id);
        return ResponseBuilder.buildSuccessResponse("Space deleted successfully");
    }
}
