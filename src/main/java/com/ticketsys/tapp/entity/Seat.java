package com.ticketsys.tapp.entity;

import com.ticketsys.tapp.data.SeatStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Seat {
    @Id
    @EqualsAndHashCode.Include
    private UUID id = UUID.randomUUID();

    @Column(nullable = false)
    private BigDecimal cost;

    @Column(nullable = false)
    private String placement;

    @Column()
    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

}
