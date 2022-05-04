package ru.kinoday.front.cinema.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CinemaHall {

    private Long id;
    private String name;
    private String type;
    private List<Place> places;
    private Set<String> availableFormats;

}
