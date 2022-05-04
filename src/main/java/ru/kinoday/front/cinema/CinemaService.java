package ru.kinoday.front.cinema;

import ru.kinoday.front.cinema.model.Cinema;
import ru.kinoday.front.cinema.model.Movie;

import java.util.List;

public interface CinemaService {

    Cinema getCinemaById(long id);
    List<Cinema> getAllCinema();

    Movie getMovieById(long id);
    List<Movie> getLastMovies(int count);
    List<Movie> getAllMovies();


}
