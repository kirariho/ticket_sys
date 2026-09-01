package com.ticketsys.tapp.seat;

import com.ticketsys.tapp.entity.Event;
import com.ticketsys.tapp.entity.Seat;
import com.ticketsys.tapp.seats.SeatRepository;
import com.ticketsys.tapp.seats.SeatsMapper;
import com.ticketsys.tapp.seats.SeatsService;
import com.ticketsys.tapp.seats.dto.SeatsResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class TestSeatService {

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private SeatsMapper seatMapper;

    @InjectMocks
    private SeatsService seatService;

    @Test
    void getAllSeats_shouldReturnListOfResponses(){

        Seat seat = new Seat();
        seat.setPlacement("A1");
        List<Seat> seats = List.of(seat);

        SeatsResponse response = new SeatsResponse();
        response.setPlacement("A1");
        List<SeatsResponse> responses = List.of(response);

        when(seatRepository.findByEventId(any())).thenReturn(seats);
        when(seatMapper.toSeatResponse(seats)).thenReturn(responses);

        List<SeatsResponse> result = seatService.findAll(UUID.randomUUID());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPlacement()).isEqualTo("A1");

    }

    @Test
    void findById_returnSeatById(){
        UUID eventId = UUID.randomUUID();
        Event event = new Event();
        event.setId(eventId);

        Seat seat = new Seat();
        seat.setId(UUID.randomUUID());
        seat.setEvent(event);

        SeatsResponse response = new SeatsResponse();
        response.setId(UUID.randomUUID());

        when(seatRepository.findById(seat.getId())).thenReturn(Optional.of(seat));
        when(seatMapper.toResponse(seat)).thenReturn(response);

        SeatsResponse result = seatService.findById(eventId, seat.getId());

        assertThat(result).isNotNull();
    }

    @Test
    void findById_whenNotFound_shouldThrowException(){

        when(seatRepository.findById(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> seatService.findById(UUID.randomUUID(), UUID.randomUUID())).isInstanceOf(ResponseStatusException.class);
    }
}
