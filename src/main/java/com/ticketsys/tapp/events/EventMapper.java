package com.ticketsys.tapp.events;

import com.ticketsys.tapp.entity.Event;
import com.ticketsys.tapp.events.dto.EventsResponse;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface EventMapper {
    List<EventsResponse> toEventsResponse (List<Event> event);
    EventsResponse toResponse (Event event);
}
