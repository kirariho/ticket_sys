package com.ticketsys.tapp.ticket;

import com.ticketsys.tapp.data.SeatStatus;
import com.ticketsys.tapp.data.TicketStatus;
import com.ticketsys.tapp.entity.Seat;
import com.ticketsys.tapp.entity.Ticket;
import com.ticketsys.tapp.entity.User;
import com.ticketsys.tapp.seats.SeatRepository;
import com.ticketsys.tapp.tickets.TicketRepository;
import com.ticketsys.tapp.tickets.TicketsMapper;
import com.ticketsys.tapp.tickets.TicketsService;
import com.ticketsys.tapp.tickets.dto.TicketsResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestTicketService {

    @InjectMocks
    private TicketsService ticketsService;

    @Mock
    private TicketsMapper ticketsMapper;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private SeatRepository seatRepository;



    @Test
    void getAllTickets_shouldReturnListOfResponses(){

        Ticket ticket = new Ticket();
        ticket.setBuyingTime(LocalDateTime.of(2026, 1, 1, 12, 0));
        List<Ticket> tickets = List.of(ticket);

        TicketsResponse response = new TicketsResponse();
        response.setBuyingTime(LocalDateTime.of(2026, 1, 1, 12, 0));
        List<TicketsResponse> responses = List.of(response);

        when(ticketRepository.findByUserId(any())).thenReturn(tickets);
        when(ticketsMapper.toResponseList(tickets)).thenReturn(responses);

        List<TicketsResponse> result = ticketsService.findAllByUserId(UUID.randomUUID());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getBuyingTime()).isEqualTo(LocalDateTime.of(2026, 1, 1, 12, 0));
    }

    @Test
    void findById_returnTicketById(){
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);

        UUID ticketId = UUID.randomUUID();
        Ticket ticket = new Ticket();
        ticket.setId(ticketId);
        ticket.setUser(user);

        TicketsResponse response = new TicketsResponse();
        response.setId(UUID.randomUUID());

        when(ticketRepository.findById(ticket.getId())).thenReturn(Optional.of(ticket));
        when(ticketsMapper.toResponse(ticket)).thenReturn(response);

        TicketsResponse result = ticketsService.findById(userId, ticketId);

        assertThat(result).isSameAs(response);
    }

    @Test
    void findById_whenNotFound_shouldThrowException(){

        when(ticketRepository.findById(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> ticketsService.findById(UUID.randomUUID(), UUID.randomUUID()))
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        e -> assertThat(e.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND));
    }

   @Test
    void findById_whenTicketBelongsToAnotherUser_throwsNotFound(){
        UUID strangerId = UUID.randomUUID();

        User owner = new User();
        owner.setId(UUID.randomUUID());

        Ticket ticket = new Ticket();
        ticket.setId(UUID.randomUUID());
        ticket.setUser(owner);

        when(ticketRepository.findById(ticket.getId())).thenReturn(Optional.of(ticket));
        assertThatThrownBy(() -> ticketsService.findById(strangerId, ticket.getId()))
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        e -> assertThat(e.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND));

    }

    @Test
    void returnTicket_whenTicketBelongsToAnotherUser_throwsNotFound(){
        UUID strangerId = UUID.randomUUID();

        User owner = new User();
        owner.setId(UUID.randomUUID());

        Ticket ticket = new Ticket();
        ticket.setId(UUID.randomUUID());
        ticket.setStatus(TicketStatus.CONFIRMED);
        ticket.setUser(owner);

        when(ticketRepository.findById(ticket.getId())).thenReturn(Optional.of(ticket));

        assertThatThrownBy(() -> ticketsService.returnTicket(strangerId, ticket.getId()))
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        e -> assertThat(e.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND));

        verify(ticketRepository, never()).save(any());
        verify(seatRepository, never()).save(any());
    }

    @Test
    void returnTicket_whenConfirmed_cancelsTicketAndFreesSeat() {
        User user = new User();
        user.setId(UUID.randomUUID());

        Seat seat = new Seat();
        seat.setId(UUID.randomUUID());
        seat.setStatus(SeatStatus.SOLD);

        Ticket ticket = new Ticket();
        ticket.setId(UUID.randomUUID());
        ticket.setUser(user);
        ticket.setSeat(seat);
        ticket.setStatus(TicketStatus.CONFIRMED);

        when(ticketRepository.findById(ticket.getId())).thenReturn(Optional.of(ticket));

        ticketsService.returnTicket(user.getId(), ticket.getId());

        assertThat(ticket.getStatus()).isEqualTo(TicketStatus.CANCELLED);
        assertThat(seat.getStatus()).isEqualTo(SeatStatus.AVAILABLE);

        verify(ticketRepository).save(ticket);
        verify(seatRepository).save(seat);
        
    }

    @Test
    void returnTicket_whenAlreadyCancelled_throwsNotAcceptable(){
        User user = new User();
        user.setId(UUID.randomUUID());

        Seat seat = new Seat();
        seat.setId(UUID.randomUUID());
        seat.setStatus(SeatStatus.AVAILABLE);

        Ticket ticket = new Ticket();
        ticket.setId(UUID.randomUUID());
        ticket.setUser(user);
        ticket.setSeat(seat);
        ticket.setStatus(TicketStatus.CANCELLED);

        when(ticketRepository.findById(ticket.getId())).thenReturn(Optional.of(ticket));

        assertThatThrownBy(() -> ticketsService.returnTicket(user.getId(), ticket.getId()))
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        e -> assertThat(e.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE));

        verify(ticketRepository, never()).save(any());
        verify(seatRepository, never()).save(any());
    }

}
