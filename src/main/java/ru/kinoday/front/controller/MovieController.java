package ru.kinoday.front.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kinoday.front.cinema.CinemaService;

@Controller
@AllArgsConstructor
@RequestMapping("/movie/")
public class MovieController {

    private CinemaService cinemaService;

    @GetMapping()
    public String index(Model m) {
        m.addAttribute("movies", cinemaService.getAllMovies());
        return "movie/all";
    }

    @GetMapping("/{id}")
    public String movieById(@PathVariable long id, Model m) {
        m.addAttribute("movie", cinemaService.getMovieById(id));
        return "movie/movie";
    }
}
