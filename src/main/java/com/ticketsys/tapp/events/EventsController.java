package com.ticketsys.tapp.events;

import com.ticketsys.tapp.events.dto.EventsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/events")
public class EventsController {

    @Autowired
    protected EventsService eventService;

    @GetMapping("/{id}")
    public EventsResponse getEventById(@PathVariable UUID id){
        return eventService.findById(id);
    }

    @GetMapping("/")
    public List<EventsResponse> getAllEvents(){
        return eventService.findAll();
    }

}
