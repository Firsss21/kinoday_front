package ru.kinoday.front.cinema.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Show {
    private Map<Integer, Map<Integer, TicketPlace>> places;
    private Long cinemaId;
    private ScheduleElement scheduleElement;
    private Movie movie;

    public TicketPlace getTicketPlace(Place place) {
        return places.get(place.getRow()).get(place.getPlace());
    }
}
