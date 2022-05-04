package ru.kinoday.front.cinema.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.kinoday.front.cinema.CinemaService;
import ru.kinoday.front.cinema.model.Cinema;
import ru.kinoday.front.cinema.model.Movie;
import ru.kinoday.front.news.entity.News;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CinemaRemoteService implements CinemaService {

    public CinemaRemoteService(@Autowired RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private RestTemplate restTemplate;

    @Value("${service.cinema.uri}")
    private String hostAddress;

    private final String pathMovie = "/movie/";
    private final String pathCinema = "/cinema/";

    @Override
    public Cinema getCinemaById(long id) {
        ResponseEntity<Cinema> response = restTemplate.getForEntity(hostAddress + pathCinema + id, Cinema.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            return null;
        }
    }

    @Override
    public List<Cinema> getAllCinema() {
        ResponseEntity<Cinema[]> response = restTemplate.getForEntity(hostAddress + pathCinema, Cinema[].class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return Arrays.stream(response.getBody()).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public Movie getMovieById(long id) {
        ResponseEntity<Movie> response = restTemplate.getForEntity(hostAddress + pathMovie + id, Movie.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            return null;
        }
    }

    @Override
    public List<Movie> getLastMovies(int count) {
        ResponseEntity<Movie[]> response = restTemplate.getForEntity(hostAddress + pathMovie + "last/?count=" + count, Movie[].class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return Arrays.stream(response.getBody()).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Movie> getAllMovies() {
        ResponseEntity<Movie[]> response = restTemplate.getForEntity(hostAddress + pathMovie, Movie[].class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return Arrays.stream(response.getBody()).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }


}
