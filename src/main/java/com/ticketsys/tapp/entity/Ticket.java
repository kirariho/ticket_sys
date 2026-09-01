package com.ticketsys.tapp.entity;

import com.ticketsys.tapp.data.TicketStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
public class Ticket {

    @Id
    private UUID id = UUID.randomUUID();

    @Column(name = "buying_time")
    private LocalDateTime buyingTime;

    @Column(nullable  = false)
    private String qrCode;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", referencedColumnName = "id")
    private Seat seat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
