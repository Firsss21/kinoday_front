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
}

@AllArgsConstructor
@Getter
enum Format {
    type_2D("2D"),
    type_2D_ATMOS("2D ATMOS"),
    type_3D("3D"),
    type_3D_IMAX("3D IMAX"),
    type_3D_ATMOS("3D ATMOS"),
    ;
    private String description;
}
