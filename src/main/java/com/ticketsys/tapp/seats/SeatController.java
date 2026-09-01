package com.ticketsys.tapp.seats;

import com.ticketsys.tapp.seats.dto.SeatsResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/events/{eventId}/seats")
public class SeatController {

    private final SeatsService seatService;

    public SeatController(SeatsService seatService){
        this.seatService = seatService;
    }

    @GetMapping("/{seatId}")
    public SeatsResponse getSeatById(@PathVariable  UUID eventId, @PathVariable  UUID seatId){return seatService.findById(eventId, seatId);}

    @GetMapping()
    public List<SeatsResponse> getAllSeats(@PathVariable  UUID eventId){return seatService.findAll(eventId);}

}
