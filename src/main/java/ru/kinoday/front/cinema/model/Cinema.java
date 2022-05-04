package ru.kinoday.front.cinema.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Cinema {

    private Long id;
    private List<CinemaHall> cinemaHallList;
    private String description;
    private String imagePath;
    private String name;
}
