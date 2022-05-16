package ru.kinoday.front.cinema.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class Movie {
    private Long id;
    private String name;
    private String description;
    private String mainImagePath;
    private Genre genre;
    private String country;
    private String year;
    private Date added;
    private Long duration;
    private String director;
    private String trailer;
    private int ageRating;
    private float ratingKp;
    private float ratingImdb;
}

@AllArgsConstructor
@Getter
enum Genre {
    DRAMA("Драма"),
    TRILLER("Триллер"),
    FANTASY("Фэнтези"),
    ROMANCE("Мелодрама"),
    ACTION("Экшн"),
    CRIME("Криминал"),
    HISTORICAL("Исторический"),
    HORROR("Ужастик"),
    SCIENCE("Научный"),
    ANIMATION("Мультфильм"),
    WESTERN("Вестерн"),
    COMEDY("Комедия"),
    BIOGRAPHY("Биографический"),
    DETECTIVE("Детектив"),
    ;

    private final String description;
}
