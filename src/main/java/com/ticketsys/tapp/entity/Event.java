package com.ticketsys.tapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Event {

    @Id
    @EqualsAndHashCode.Include
    private UUID id = UUID.randomUUID();

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String place;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event")
    private Set<Seat> seats;

}
