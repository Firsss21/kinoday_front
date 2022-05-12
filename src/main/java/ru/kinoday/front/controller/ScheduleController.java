package ru.kinoday.front.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kinoday.front.cinema.CinemaService;
import ru.kinoday.front.cinema.ScheduleService;
import ru.kinoday.front.cinema.model.Schedule;
import ru.kinoday.front.cinema.model.Show;

@Controller
@AllArgsConstructor
@RequestMapping("/schedule/")
public class ScheduleController {

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
    public String getShow(@PathVariable long id, Model m) {
        Show show = scheduleService.getShow(id);
        m.addAttribute("show", show);
        return "schedule/show";
    }
}
