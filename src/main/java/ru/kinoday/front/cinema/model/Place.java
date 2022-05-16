package ru.kinoday.front.cinema.model;

import lombok.*;

import java.io.Serializable;



@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class Place {
    private int row;
    private int place;
}

