package ru.kinoday.front.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kinoday.front.cinema.CinemaService;
import ru.kinoday.front.cinema.ScheduleService;
import ru.kinoday.front.cinema.model.Movie;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Controller
@AllArgsConstructor
@RequestMapping("/movie/")
public class MovieController {

    private CinemaService cinemaService;
    private ScheduleService scheduleService;

    @GetMapping("/")
    public String index(Model m) {
        m.addAttribute("movies", cinemaService.getAllMovies());
        return "movie/all";
    }

    @GetMapping("/{id}")
    public String movieById(@PathVariable long id, Model m) {
        Movie movie = cinemaService.getMovieById(id);
        m.addAttribute("movie", movie);
        m.addAttribute("duration", java.time.LocalDate.now().atTime(0,0,0).plusSeconds(movie.getDuration() / 1000).format(DateTimeFormatter.ofPattern("HH:mm")));
        return "movie/movie";
    }
}
