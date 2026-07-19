package com.company.coworking.management.entity;

import com.company.coworking.management.common.auditing.AuditableEntity;
import com.company.coworking.management.enums.SpaceType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SPACE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Space extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false, length = 150)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false, length = 30)
    private SpaceType type;

    @Column(name = "HOURLY_RATE", nullable = false, precision = 10, scale = 2)
    private BigDecimal hourlyRate;

    @Builder.Default
    @OneToMany(mappedBy = "space", fetch = FetchType.LAZY)
    private List<Reservation> reservations = new ArrayList<>();
}