package com.ticketsys.tapp.tickets.dto;

import com.ticketsys.tapp.data.TicketStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
public class TicketsResponse {
    private UUID id;
    private TicketStatus ticketStatus;
    private LocalDateTime buyingTime;
    private String seatPlacement;
    private LocalDateTime eventDate;
    private String eventTitle;
}
