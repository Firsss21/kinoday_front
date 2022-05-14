package ru.kinoday.front.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kinoday.front.cinema.CinemaService;
import ru.kinoday.front.cinema.ScheduleService;
import ru.kinoday.front.cinema.model.Schedule;
import ru.kinoday.front.cinema.model.Show;
import ru.kinoday.front.order.entity.Order;
import ru.kinoday.front.order.service.OrderService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@AllArgsConstructor
@RequestMapping("/schedule/")
public class ScheduleController {


    private OrderService orderService;
    private ObjectFactory<HttpSession> httpSessionFactory;
    private ScheduleService scheduleService;
    private CinemaService cinemaService;

    @GetMapping
    public String getDefaultSchedule(Model m) {
        Schedule schedule = scheduleService.getSchedule();
        m.addAttribute("schedule", schedule);
        m.addAttribute("plusDays", 0);
        m.addAttribute("cinemaList", cinemaService.getAllCinema());
        return "schedule/schedule";
    }

    @GetMapping("/{id}")
    public String getCinemaSchedule(@PathVariable long id, @RequestParam(name = "plusDays", defaultValue = "0") int plusDays, Model m) {
        Schedule schedule = scheduleService.getScheduleForAnotherDay(id, plusDays);
        m.addAttribute("schedule", schedule);
        m.addAttribute("plusDays", plusDays);
        m.addAttribute("cinemaList", cinemaService.getAllCinema());
        return "schedule/schedule";
    }

    @GetMapping("/show/{id}")
    public String getShow(@PathVariable long id,
                          Model m,
                          Order order
    ) {
        Show show = scheduleService.getShow(id);
        m.addAttribute("show", show);
        m.addAttribute("order", order);
        m.addAttribute("cinema", cinemaService.getCinemaById(show.getCinemaId()));
        return "schedule/show";
    }

    @PostMapping("/order/{scheduleId}")
    public String newOrder(@Valid @ModelAttribute("order") Order order,
                           BindingResult result,
                           @PathVariable int scheduleId,
                           Model m
    ) {
        System.out.println(order);

        if (result.hasErrors()) {
            Show show = scheduleService.getShow(scheduleId);
            m.addAttribute("error", result.getAllErrors().get(0).getDefaultMessage());
            m.addAttribute("show", show);
            m.addAttribute("order", order);
            m.addAttribute("cinema", cinemaService.getCinemaById(show.getCinemaId()));
            return "schedule/show";
        }

        boolean canOrder = orderService.checkOrder(order);
        if (!canOrder) {
            System.out.println("cant order");
            return "redirect:/schedule/show/{scheduleId}";
        }

        System.out.println("success ");
        HttpSession session = httpSessionFactory.getObject();

        System.out.println(session);

        session.getAttributeNames().asIterator().forEachRemaining(System.out::println);

        return "redirect:/schedule/show/{scheduleId}";

        // scheduleId
        // places

        // agreement
    }
}
