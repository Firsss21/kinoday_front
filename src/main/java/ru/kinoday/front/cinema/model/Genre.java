package ru.kinoday.front.cinema.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Genre {
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