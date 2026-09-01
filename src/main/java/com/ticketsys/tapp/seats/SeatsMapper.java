package com.ticketsys.tapp.seats;

import com.ticketsys.tapp.entity.Seat;
import com.ticketsys.tapp.seats.dto.SeatsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SeatsMapper {
    List<SeatsResponse> toSeatResponse(List<Seat> seats);

    @Mapping(target = "eventId", source = "event.id")
    SeatsResponse toResponse(Seat seat);

}
