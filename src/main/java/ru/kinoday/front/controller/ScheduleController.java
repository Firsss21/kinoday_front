package ru.kinoday.front.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kinoday.front.cinema.CinemaService;
import ru.kinoday.front.cinema.ScheduleService;

@Controller
@AllArgsConstructor
@RequestMapping("/schedule/")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;


}
