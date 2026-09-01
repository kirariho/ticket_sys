package com.ticketsys.tapp.seats.dto;

import com.ticketsys.tapp.data.SeatStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class SeatsResponse {
    private UUID id;
    private BigDecimal cost;
    private String placement;
    private SeatStatus status;
    private UUID eventId;
}
