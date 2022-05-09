package ru.kinoday.front.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kinoday.front.cinema.CinemaService;
import ru.kinoday.front.news.service.NewsService;

@Controller
@AllArgsConstructor
@RequestMapping("/cinema/")
public class CinemaController {

    @Autowired
    private CinemaService cinemaService;

    @GetMapping
    public String index(Model m) {
        m.addAttribute("cinemas", cinemaService.getAllCinema());
        return "cinema/all";
    }

}
