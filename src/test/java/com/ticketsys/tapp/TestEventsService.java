package com.ticketsys.tapp;

import com.ticketsys.tapp.entity.Event;
import com.ticketsys.tapp.events.EventMapper;
import com.ticketsys.tapp.events.EventRepository;
import com.ticketsys.tapp.events.EventsService;
import com.ticketsys.tapp.events.dto.EventsResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestEventsService {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventsService eventService;
    @Test
    void findAll_shouldReturnListOfResponses(){
        Event event = new Event();
        event.setTitle("Concert_name");
        List<Event> events = List.of(event);

        EventsResponse response = new EventsResponse();
        response.setTitle("Concert_name");
        List<EventsResponse> responses = List.of(response);

        when(eventRepository.findAll()).thenReturn(events);
        when(eventMapper.toEventsResponse(events)).thenReturn(responses);

        List<EventsResponse> result = eventService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Concert_name");

    }

    @Test
    void findById_whenNotFound_shouldThrowException(){

        when(eventRepository.findById(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> eventService.findById(UUID.randomUUID())).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void findById_returnEventById(){
        Event event = new Event();
        event.setId(UUID.randomUUID());

        EventsResponse response = new EventsResponse();
        response.setId(event.getId());

        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
        when(eventMapper.toResponse(event)).thenReturn(response);

        EventsResponse result = eventService.findById(event.getId());

        assertThat(result).isNotNull();
    }

}
