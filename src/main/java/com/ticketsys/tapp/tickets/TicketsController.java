package com.ticketsys.tapp.tickets;

import com.ticketsys.tapp.tickets.dto.TicketsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users/{userId}/tickets")
public class TicketsController {
    private final TicketsService ticketsService;

    public TicketsController(TicketsService ticketsService){
        this.ticketsService = ticketsService;
    }

    @GetMapping()
    public List<TicketsResponse> getAllTicketsByUserId(@PathVariable UUID userId){
        return ticketsService.findAllByUserId(userId);
    }

    @GetMapping("/{ticketId}")
    public TicketsResponse getTicketById(
            @PathVariable UUID userId,
            @PathVariable UUID ticketId){
        return ticketsService.findById(userId, ticketId);
    }

    @PatchMapping("/{ticketId}/return")
    @ResponseStatus(HttpStatus.OK)
    public void returnTicket(
            @PathVariable UUID userId,
            @PathVariable UUID ticketId){
        ticketsService.returnTicket(userId, ticketId);
    }

}
