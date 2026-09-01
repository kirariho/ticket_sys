package com.ticketsys.tapp.events.dto;


import lombok.Data;


import java.util.UUID;

@Data
public class EventsResponse {

    private UUID id;
    private String title;
    private String place;

}
