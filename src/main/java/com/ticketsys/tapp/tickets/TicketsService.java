package com.ticketsys.tapp.tickets;

import com.ticketsys.tapp.data.SeatStatus;
import com.ticketsys.tapp.data.TicketStatus;
import com.ticketsys.tapp.entity.Ticket;
import com.ticketsys.tapp.seats.SeatRepository;
import com.ticketsys.tapp.tickets.dto.TicketsResponse;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class TicketsService {
    private final TicketsMapper ticketsMapper;
    private final TicketRepository ticketRepository;
    private final SeatRepository seatRepository;

    public TicketsService(TicketsMapper ticketsMapper, TicketRepository ticketRepository, SeatRepository seatRepository){
        this.ticketsMapper = ticketsMapper;
        this.ticketRepository = ticketRepository;
        this.seatRepository = seatRepository;
    }

    @Transactional
    public List<TicketsResponse> findAllByUserId(UUID userId){

       List<Ticket> tickets = ticketRepository.findByUserId(userId);
       List<TicketsResponse> getTicketsResponseResult = ticketsMapper.toResponseList(tickets);
       return getTicketsResponseResult;

    }

    @Transactional
    public TicketsResponse findById(UUID userId, UUID ticketId){
        Ticket ticket = getTicketForUser(userId, ticketId);
        TicketsResponse getTicketResponseResult = ticketsMapper.toResponse(ticket);
        return getTicketResponseResult;
    }

    @Transactional
    public void returnTicket(UUID userId, UUID ticketId){
        Ticket ticket = getTicketForUser(userId, ticketId);

        if(!ticket.getStatus().equals(TicketStatus.CONFIRMED)){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        }
        ticket.setStatus(TicketStatus.CANCELLED);
        ticket.getSeat().setStatus(SeatStatus.AVAILABLE);

        seatRepository.save(ticket.getSeat());
        ticketRepository.save(ticket);

    }

    private Ticket getTicketForUser(UUID userId, UUID ticketId){
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if(!ticket.getUser().getId().equals(userId)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "У данного пользователя нет такого билета");
        }
        return ticket;
    }
}
