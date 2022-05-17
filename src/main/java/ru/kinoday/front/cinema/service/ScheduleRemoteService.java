package ru.kinoday.front.cinema.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.kinoday.front.cinema.CinemaService;
import ru.kinoday.front.cinema.ScheduleService;
import ru.kinoday.front.cinema.model.Cinema;
import ru.kinoday.front.cinema.model.Schedule;
import ru.kinoday.front.cinema.model.ScheduleDTO;
import ru.kinoday.front.cinema.model.Show;
import ru.kinoday.front.cinema.model.dto.ScheduleElementDto;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScheduleRemoteService implements ScheduleService {

    private final CinemaService cinemaService;
    private final RestTemplate restTemplate;

    @Value("${service.cinema.uri}")
    private String hostAddress;

    private final String pathSchedule = "/schedule/";

    public ScheduleRemoteService(CinemaService cinemaService, RestTemplate restTemplate) {
        this.cinemaService = cinemaService;
        this.restTemplate = restTemplate;
    }

    @Override
    public Schedule getScheduleToday(Long cinemaId) {
        return getSchedule(cinemaId);
    }

    @Override
    public Schedule getScheduleTomorrow(Long cinemaId) {
        return getSchedule(Timestamp.valueOf(LocalDate.now().atTime(0, 0, 0).plusDays(1)), cinemaId);
    }

    @Override
    public Schedule getScheduleForAnotherDay(Long cinemaId, int plusDays) {
        return getSchedule(Timestamp.valueOf(LocalDate.now().atTime(0, 0, 0).plusDays(plusDays)), cinemaId);
    }

    @Override
    public Schedule getSchedule() {
        List<Cinema> allCinema = cinemaService.getAllCinema();
        if (allCinema.isEmpty())
            return Schedule.empty();

        Long cinemaId = allCinema.get(0).getId();
        // first cinema from list
        return getSchedule(cinemaId);
    }

    private Schedule getSchedule(long cinemaId) {
        // today
        return getSchedule(Timestamp.valueOf(LocalDate.now().atTime(0, 0, 0)) ,cinemaId);
    }


    private Schedule getSchedule(Timestamp from, long cinemaId) {
//      to endTime of day
        return getSchedule(from, Timestamp.valueOf(from.toLocalDateTime().toLocalDate().atTime(23, 59,59)),cinemaId);
    }

    private Schedule getSchedule(Timestamp from, Timestamp to, long cinemaId) {
        String urlTemplate = UriComponentsBuilder.fromHttpUrl(hostAddress + pathSchedule)
                .queryParam("from", "{from}")
                .queryParam("to", "{to}")
                .queryParam("id", "{id}")
                .encode()
                .toUriString();

        Map<String, String> variables = new HashMap<>();
        variables.put("from", from.toString());
        variables.put("to", to.toString());
        variables.put("id", String.valueOf(cinemaId));
        try {
            ResponseEntity<ScheduleDTO> response = restTemplate.getForEntity(
                    urlTemplate,
                    ScheduleDTO.class,
                    variables
            );
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody().toSchedule();
            } else {
                return Schedule.empty();
            }
        } catch (RestClientException e) {
            return Schedule.empty();
        }
    }

    public Show getShow(long scheduleId) {
        ResponseEntity<Show> response = restTemplate.getForEntity(hostAddress + pathSchedule + scheduleId, Show.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            return new Show();
        }
    }

    @Override
    public ResponseEntity<String> addSchedule(ScheduleElementDto elementDto) {
        try {
            return restTemplate.postForEntity(hostAddress + pathSchedule, elementDto, String.class);
        } catch (RestClientException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
