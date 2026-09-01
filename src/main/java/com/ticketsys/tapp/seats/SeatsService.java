package com.ticketsys.tapp.seats;

import com.ticketsys.tapp.entity.Event;
import com.ticketsys.tapp.entity.Seat;
import com.ticketsys.tapp.events.EventRepository;
import com.ticketsys.tapp.seats.dto.SeatsResponse;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class SeatsService {
    private final SeatsMapper seatsMapper;
    private final SeatRepository seatRepository;
    private final EventRepository eventRepository;

    public SeatsService(SeatsMapper seatsMapper, SeatRepository seatRepository, EventRepository eventRepository ){
        this.seatsMapper = seatsMapper;
        this.seatRepository = seatRepository;
        this.eventRepository = eventRepository;
    }

    @Transactional
    public List<SeatsResponse> findAll(UUID eventId){

        List<Seat> seats = seatRepository.findByEventId(eventId);
        List<SeatsResponse> getSeatResponseResult = seatsMapper.toSeatResponse(seats);
        return getSeatResponseResult;

    }

    @Transactional
    public SeatsResponse findById(UUID eventId, UUID seatId){

        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if(!seat.getEvent().getId().equals(eventId)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "У выбранного события нет такого места");
        }

        SeatsResponse getSeatResponseResult = seatsMapper.toResponse(seat);
        return getSeatResponseResult;

    }
}
