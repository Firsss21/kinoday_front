package ru.kinoday.front.cinema.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
class PlaceId implements Serializable {
    private int row;
    private int place;
}

@NoArgsConstructor
@AllArgsConstructor
public class Place {
    private PlaceId placeId;

    public int getRow() {
        return placeId.getRow();
    }

    public int getPlace() {
        return placeId.getPlace();
    }
}
