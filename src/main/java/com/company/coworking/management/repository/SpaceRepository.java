package com.company.coworking.management.repository;

import com.company.coworking.management.entity.Space;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SpaceRepository extends JpaRepository<Space, Long> {

    @NonNull
    @Query("""
            SELECT DISTINCT s
            FROM Space s
            LEFT JOIN FETCH s.reservations
            """)
    List<Space> findAll();

    @NonNull
    @Query("""
            SELECT s
            FROM Space s
            LEFT JOIN FETCH s.reservations
            WHERE s.id = :id
            """)
    Optional<Space> findById(@NonNull Long id);
}