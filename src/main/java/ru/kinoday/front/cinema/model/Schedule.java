package ru.kinoday.front.cinema.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Schedule {

    private Timestamp fromDate;

    private Timestamp toDate;

    private long cinemaId;

    private Map<Movie, List<ScheduleElement>> data;

    public static Schedule empty() {
        return new Schedule(
                Timestamp.valueOf(LocalDate.now().atTime(0,0,0)),
                Timestamp.valueOf(LocalDate.now().atTime(23,59,59)),
                0L,
                new HashMap<>()
        );
    }
}
