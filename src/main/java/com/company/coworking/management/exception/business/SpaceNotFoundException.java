package com.company.coworking.management.exception.business;

public class SpaceNotFoundException extends RuntimeException {
    public SpaceNotFoundException(Long id) {
        super("Space not found with id " + id);
    }
}
