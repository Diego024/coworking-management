package com.company.coworking.management.repository;

import com.company.coworking.management.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
            "FROM Reservation r " +
            "WHERE r.space.id = :spaceId " +
            "AND r.status != 'CANCELLED' " +
            "AND r.startTime < :endTime " +
            "AND r.endTime > :startTime")
    boolean existsOverlappingReservation(@Param("spaceId") Long spaceId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime
    );
}