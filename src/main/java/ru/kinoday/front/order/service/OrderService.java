package ru.kinoday.front.order.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kinoday.front.cinema.ScheduleService;
import ru.kinoday.front.cinema.model.Place;
import ru.kinoday.front.cinema.model.Show;
import ru.kinoday.front.cinema.model.TicketPlace;
import ru.kinoday.front.order.entity.Order;

import java.util.Date;

@Service
@AllArgsConstructor
public class OrderService {

    private ScheduleService scheduleService;

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

        if (show.getScheduleElement().getStartTime().after(new Date())) {
            return false;
        }

        return true;
    }
}
