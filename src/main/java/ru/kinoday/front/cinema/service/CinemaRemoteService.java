package ru.kinoday.front.cinema.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.kinoday.front.cinema.CinemaService;
import ru.kinoday.front.cinema.model.Cinema;
import ru.kinoday.front.cinema.model.Movie;
import ru.kinoday.front.cinema.model.dto.MovieDto;
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
        try {
            ResponseEntity<Cinema> response = restTemplate.getForEntity(hostAddress + pathCinema + id, Cinema.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                return null;
            }
        } catch (RestClientException e) {
            return null;
        }
    }

    @Override
    public List<Cinema> getAllCinema() {
        try {
            ResponseEntity<Cinema[]> response = restTemplate.getForEntity(hostAddress + pathCinema, Cinema[].class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return Arrays.stream(response.getBody()).collect(Collectors.toList());
            } else {
                return new ArrayList<>();
            }
        } catch (RestClientException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public Movie getMovieById(long id) {
        try {
            ResponseEntity<Movie> response = restTemplate.getForEntity(hostAddress + pathMovie + id, Movie.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                return null;
            }
        } catch (RestClientException e) {
            return null;
        }
    }

    @Override
    public List<Movie> getLastMovies(int count) {
        try {
            ResponseEntity<Movie[]> response = restTemplate.getForEntity(hostAddress + pathMovie + "last/?count=" + count, Movie[].class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return Arrays.stream(response.getBody()).collect(Collectors.toList());
            } else {
                return new ArrayList<>();
            }
        } catch (RestClientException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Movie> getAllMovies() {
        try {
            ResponseEntity<Movie[]> response = restTemplate.getForEntity(hostAddress + pathMovie, Movie[].class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return Arrays.stream(response.getBody()).collect(Collectors.toList());
            } else {
                return new ArrayList<>();
            }
        } catch (RestClientException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public boolean addMovie(MovieDto movie) {
        try {
            restTemplate.postForEntity(hostAddress + pathMovie, movie, String.class);
            return true;
        } catch (RestClientException e) {
            return false;
        }
    }

    @Override
    public boolean deleteMovie(Long id) {
        try {
            restTemplate.delete(hostAddress + pathMovie + id);
            return true;
        } catch (RestClientException e) {
            return false;
        }
    }


}
