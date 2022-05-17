package ru.kinoday.front.order.service;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.kinoday.front.cinema.ScheduleService;
import ru.kinoday.front.cinema.model.Place;
import ru.kinoday.front.cinema.model.Show;
import ru.kinoday.front.cinema.model.TicketPlace;
import ru.kinoday.front.order.entity.Order;
import ru.kinoday.front.order.entity.Ticket;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    public OrderService(ScheduleService scheduleService, RestTemplate restTemplate) {
        this.scheduleService = scheduleService;
        this.restTemplate = restTemplate;
    }

    private ScheduleService scheduleService;
    private RestTemplate restTemplate;

    @Value("${service.cinema.uri}")
    private String hostAddress;

    private final String path = "/tickets/";

    public boolean checkOrder(Order order) {
        Show show = scheduleService.getShow(order.getScheduleId());
        if (show == null)
            return false;

        if (order.getTickets().size() <= 0)
            return false;

        for (Place place : order.getTickets()) {
            TicketPlace ticketPlace = show.getTicketPlace(place);
            if (!ticketPlace.isCanOrder())
                return false;
        }

        if (show.getScheduleElement().isStarted()) {
            return false;
        }

        return true;
    }

    public String paymentRequest(Order order, String email, long uid) {
        List<Ticket> tickets = orderNewTickets(order, email);
        return getPaymentLink(tickets, uid);
    }

    public List<Ticket> orderNewTickets(Order order, String email) {

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(hostAddress + path + "order")
                .queryParam("email", "{email}")
                .queryParam("order", "{order}")
                .encode()
                .toUriString();

        Gson gson = new Gson();
        Map<String, String> variables = new HashMap<>();
        variables.put("email", email);
        variables.put("order", gson.toJson(order));

        ResponseEntity<Ticket[]> response = restTemplate.getForEntity(
                urlTemplate,
                Ticket[].class,
                variables
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            return Arrays.stream(response.getBody()).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
        // send order and email
        // create ticket for every place with email
        // return this tickets
    }

    public String getPaymentLink(List<Ticket> tickets, long uid) {
        if (tickets.isEmpty())
            return "redirect:/profile";
        String urlTemplate = UriComponentsBuilder.fromHttpUrl(hostAddress + path + "pay")
                .queryParam("ticketIds", "{ticketIds}")
                .queryParam("uid", "{uid}")
                .encode()
                .toUriString();

        String s = Arrays.toString(tickets.stream().map(Ticket::getId).distinct().toArray());
        s = s.substring(1, s.length() - 1);
        Map<String, String> variables = Map.of("ticketIds", s, "uid", String.valueOf(uid));

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(
                    urlTemplate,
                    String.class,
                    variables
            );

        if (response.getStatusCode().is2xxSuccessful()) {
            return "redirect:" + response.getBody();
        } else {
            return "redirect:/profile";
        }

        } catch (RestClientException e) {
            e.printStackTrace();
            return "redirect:/profile";
        }
    }

    public List<Ticket> getTicketByEmail(String email) {
        String urlTemplate = UriComponentsBuilder.fromHttpUrl(hostAddress + path)
                .queryParam("email", "{email}")
                .encode()
                .toUriString();

        Map<String, String> variables = new HashMap<>();
        variables.put("email", email);

        ResponseEntity<Ticket[]> response = restTemplate.getForEntity(
                urlTemplate,
                Ticket[].class,
                variables
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            return Arrays.stream(response.getBody()).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    public Ticket getTicketByHash(String hash) {
        String urlTemplate = UriComponentsBuilder.fromHttpUrl(hostAddress + path + "hash")
                .queryParam("hash", "{hash}")
                .encode()
                .toUriString();

        Map<String, String> variables = new HashMap<>();
        variables.put("hash", hash);

        System.out.println(hash);

        ResponseEntity<Ticket> response = restTemplate.getForEntity(
                urlTemplate,
                Ticket.class,
                variables
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            return null;
        }
    }

    public Ticket useTicket(long id) {
        String urlTemplate = UriComponentsBuilder.fromHttpUrl(hostAddress + path + "use")
                .queryParam("id", "{id}")
                .encode()
                .toUriString();

        Map<String, Long> variables = new HashMap<>();
        variables.put("id", id);

        ResponseEntity<Ticket> response = restTemplate.getForEntity(
                urlTemplate,
                Ticket.class,
                variables
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            Ticket body = response.getBody();
            return body;
        } else {
            return null;
        }
    }
}
