package ru.kinoday.front.cinema.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ScheduleElement {
    private Long id;
    private Timestamp startTime;
    private Timestamp endTime;
    private CinemaHall hall;
    private String format;
    private int price;
}
