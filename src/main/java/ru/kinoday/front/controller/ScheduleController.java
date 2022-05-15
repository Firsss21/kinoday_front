package ru.kinoday.front.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.session.InvalidSessionAccessDeniedHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kinoday.front.cinema.CinemaService;
import ru.kinoday.front.cinema.ScheduleService;
import ru.kinoday.front.cinema.model.Schedule;
import ru.kinoday.front.cinema.model.Show;
import ru.kinoday.front.common.model.User;
import ru.kinoday.front.common.validation.ValidEmail;
import ru.kinoday.front.order.entity.Order;
import ru.kinoday.front.order.service.OrderService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@AllArgsConstructor
@RequestMapping("/schedule/")
public class ScheduleController {


    private OrderService orderService;
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
                           @RequestParam(required = false, name = "email", defaultValue = "default") String email,
                           BindingResult result,
                           @PathVariable int scheduleId,
                           Model m,
                           HttpSession session
    ) {
        if (result.hasErrors()) {
            Show show = scheduleService.getShow(scheduleId);
            if (!result.getAllErrors().isEmpty()) {
                m.addAttribute("error", result.getAllErrors().get(0));
            }
            m.addAttribute("show", show);
            m.addAttribute("order", order);
            m.addAttribute("cinema", cinemaService.getCinemaById(show.getCinemaId()));
            return "schedule/show";
        }

        boolean canOrder = orderService.checkOrder(order);
        if (!canOrder) {
            return "redirect:/schedule/show/{scheduleId}";
        }

        Object user = session.getAttribute("user");

        if (user instanceof User){
            // send to payment
            User userData = (User) user;
            return sendToPayment(order, userData.getEmail());
        } else {

            if (!email.equals("default"))
                return sendToPayment(order, email);

            // send to form without user login for post email
            return orderWithoutEmail(order, m);
        }
    }

    private String orderWithoutEmail(Order order, Model m) {
        Show show = scheduleService.getShow(order.getScheduleId());
        m.addAttribute("show", show);
        m.addAttribute("order", order);
        m.addAttribute("price", order.getTickets().size() * show.getScheduleElement().getPrice());
        m.addAttribute("cinema", cinemaService.getCinemaById(show.getCinemaId()));
        return "/order/withoutEmail";
    }

    private String sendToPayment(Order order, String email) {
        if (orderService.checkOrder(order)) {
            return orderService.paymentRequest(order, email);
//            return "redirect:https://google.com";
        } else {
            return "redirect:/schedule/";
        }
    }
}
