package ru.kinoday.front.cinema.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ScheduleElement {
    private Long id;
    private Date startTime;
    private Date endTime;
    private CinemaHall hall;
    private Format format;
    private int price;
    private boolean started;
    private Movie movie;
}

