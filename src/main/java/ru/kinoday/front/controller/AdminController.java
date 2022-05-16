package ru.kinoday.front.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kinoday.front.cinema.model.dto.MovieDto;
import ru.kinoday.front.cinema.model.dto.NewsDto;
import ru.kinoday.front.cinema.model.dto.ScheduleElementDto;
import ru.kinoday.front.cinema.model.dto.ServerStatus;
import ru.kinoday.front.order.entity.Ticket;

@Controller
@RequestMapping("/admin/")
public class AdminController {

    @GetMapping()
    public String index(
            Model m,
            @ModelAttribute NewsDto newsDto,
            @ModelAttribute ScheduleElementDto scheduleElemntDto,
            @ModelAttribute MovieDto movieDto,
            @ModelAttribute Ticket ticket
    ) {

        ServerStatus serverStatus = new ServerStatus();

        m.addAttribute("news", newsDto);
        m.addAttribute("scheduleEl", scheduleElemntDto);
        m.addAttribute("movie", movieDto);
        m.addAttribute("serverStatus", serverStatus);
        m.addAttribute("ticket", ticket);

        return "/admin/index";
    }
}
