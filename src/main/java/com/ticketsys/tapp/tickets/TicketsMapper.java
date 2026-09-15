package com.ticketsys.tapp.tickets;

import com.ticketsys.tapp.entity.Ticket;
import com.ticketsys.tapp.tickets.dto.TicketsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TicketsMapper {

    List<TicketsResponse> toResponseList(List<Ticket> tickets);

    @Mapping(source = "seat.placement", target = "seatPlacement")
    @Mapping(source = "seat.event.date", target = "eventDate")
    @Mapping(source = "seat.event.title", target = "eventTitle")
    @Mapping(source = "status", target = "ticketStatus")
    TicketsResponse toResponse(Ticket ticket);
}
