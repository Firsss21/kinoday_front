package ru.kinoday.front.cinema.model;

import lombok.*;

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

    public Place(int row, int place) {
        this.placeId = new PlaceId(row, place);
    }

    @Override
    public String toString() {
        return "Place{" +
                "row=" + placeId.getRow() + ", " +
                "place=" + placeId.getPlace() +
                '}';
    }
}
