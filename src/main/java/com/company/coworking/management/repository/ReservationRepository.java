package com.company.coworking.management.repository;

import com.company.coworking.management.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("""
            SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END
            FROM Reservation r
            WHERE r.space.id = :spaceId
                AND r.status <> ReservationStatus.CANCELLED
                AND r.startTime < :endTime
                AND r.endTime > :startTime
            """)
    boolean existsOverlappingReservation(
            @Param("spaceId") Long spaceId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    @Query("""
            SELECT r
            FROM Reservation r
                JOIN FETCH r.user
                JOIN FETCH r.space
            WHERE r.user.id = :userId
            ORDER BY r.createdAt DESC
            """)
    List<Reservation> findByUserId(@Param("userId") Long userId);

    @Query("""
            SELECT r
            FROM Reservation r
                JOIN FETCH r.user
                JOIN FETCH r.space
            WHERE r.id = :reservationId
              AND r.user.id = :userId
            """)
    Optional<Reservation> findByIdAndUserId(
            @Param("reservationId") Long reservationId,
            @Param("userId") Long userId
    );

    @Query("""
            SELECT r
            FROM Reservation r
                JOIN FETCH r.user
                JOIN FETCH r.space
            ORDER BY r.createdAt DESC
            """)
    List<Reservation> findAllOrderByCreatedAtDesc();

    @Query("""
            SELECT r
            FROM Reservation r
                JOIN FETCH r.space
            WHERE r.status <> ReservationStatus.CANCELLED
                AND r.startTime < :endDate
                AND r.endTime > :startDate
            """)
    List<Reservation> findReservationsForOccupancyReport(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}