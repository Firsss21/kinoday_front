package ru.kinoday.front.cinema.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Status {

    ACTIVE("Сервер доступен"),
    UNREACHABLE("Сервер недоступен"),
    ;

    private String description;
}
