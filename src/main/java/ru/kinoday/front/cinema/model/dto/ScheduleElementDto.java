package ru.kinoday.front.cinema.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.kinoday.front.cinema.model.Format;

import javax.persistence.EnumType;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleElementDto {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime  startTime;
    @NotNull
    private Long cinemaHallId;
    @NotNull
    private Long movieId;

    private Format format;

    @NotNull
    private Long price;
}
