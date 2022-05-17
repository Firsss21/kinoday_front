package ru.kinoday.front.cinema.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Format {
    type_2D("2D"),
    type_2D_ATMOS("2D ATMOS"),
    type_3D("3D"),
    type_3D_IMAX("3D IMAX"),
    type_3D_ATMOS("3D ATMOS"),
    ;
    private String description;
}
