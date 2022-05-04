package ru.kinoday.front.cinema.model;

import lombok.Data;

import java.util.Date;

@Data
public class Movie {
    private Long id;
    private String name;
    private String description;
    private String mainImagePath;
    private String genre;
    private String country;
    private String year;
    private Date added;
    private Long duration;
    private String director;
    private String[] descriptionImages;
    private String trailer;
    private int ageRating;
    private int kinopoiskId;
}
