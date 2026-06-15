package com.ticketsys.tapp.events;

import com.ticketsys.tapp.entity.Event;
import com.ticketsys.tapp.events.dto.EventsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class EventsService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public EventsService(EventRepository eventRepository, EventMapper eventMapper){
        this.eventMapper = eventMapper;
        this.eventRepository = eventRepository;
    }
    public List<EventsResponse> findAll(){
        List<Event> events = eventRepository.findAll();
        List<EventsResponse> getRequestResult = eventMapper.toEventsResponse(events);
        return getRequestResult;
    }
    public EventsResponse findById(UUID id){
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        EventsResponse getRequestResult = eventMapper.toResponse(event);
        return getRequestResult;

    }
}
