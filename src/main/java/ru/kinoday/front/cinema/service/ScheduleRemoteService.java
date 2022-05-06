package ru.kinoday.front.cinema.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.kinoday.front.cinema.CinemaService;
import ru.kinoday.front.cinema.ScheduleService;
import ru.kinoday.front.cinema.model.Schedule;
import ru.kinoday.front.cinema.model.ScheduleDTO;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class ScheduleRemoteService implements ScheduleService {

    @Autowired
    private CinemaService cinemaService;
    @Autowired
    private RestTemplate restTemplate;

    @Value("${service.cinema.uri}")
    private String hostAddress;

    private final String pathSchedule = "/schedule/";

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
        Long cinemaId = cinemaService.getAllCinema().get(0).getId();
        // first cinema from list
        return getSchedule(cinemaId);
    }

    private Schedule getSchedule(long cinemaId) {
        // today
        return getSchedule(Timestamp.valueOf(LocalDate.now().atTime(0, 0, 0)) ,cinemaId);
    }


    private Schedule getSchedule(Timestamp from, long cinemaId) {
//      to end of day
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

        ResponseEntity<ScheduleDTO> response = restTemplate.getForEntity(
                urlTemplate,
                ScheduleDTO.class,
                variables
        );
        System.out.println(response);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody().toSchedule();
        } else {
            return Schedule.empty();
        }
    }


}
