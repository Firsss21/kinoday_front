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
    private ScheduleElement scheduleElement;
}
