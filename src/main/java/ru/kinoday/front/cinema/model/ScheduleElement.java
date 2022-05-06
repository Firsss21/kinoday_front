package ru.kinoday.front.cinema.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ScheduleElement {
    Long id;
    Timestamp time;
    CinemaHall hall;
    String format;
    int price;
}
