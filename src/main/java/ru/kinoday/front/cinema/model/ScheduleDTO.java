package ru.kinoday.front.cinema.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import ru.kinoday.front.cinema.CinemaService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ScheduleDTO {

    private Timestamp fromDate;

    private Timestamp toDate;

    private long cinemaId;

    private Map<Long, Movie> movies;

    private Map<Long, List<ScheduleElement>> data;


    public Schedule toSchedule() {
        Map<Movie, List<ScheduleElement>> data = new HashMap<>();
        for (Map.Entry<Long, Movie> entry : this.movies.entrySet()) {
            data.put(entry.getValue(), this.data.getOrDefault(entry.getKey(), new ArrayList<>()));
        }

        return new Schedule(this.fromDate, this.toDate, this.cinemaId, data);
    }
}
