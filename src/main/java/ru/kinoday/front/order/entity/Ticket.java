package ru.kinoday.front.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kinoday.front.cinema.model.Place;
import ru.kinoday.front.cinema.model.ScheduleElement;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    private Long id;
    private ScheduleElement scheduled;
    private Place place;
    private TicketType type;
    private String personalHashCode;
    private String email;
}


