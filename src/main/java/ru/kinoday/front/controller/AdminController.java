package ru.kinoday.front.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kinoday.front.cinema.CinemaService;
import ru.kinoday.front.cinema.ScheduleService;
import ru.kinoday.front.cinema.model.*;
import ru.kinoday.front.cinema.model.dto.MovieDto;
import ru.kinoday.front.cinema.model.dto.NewsDto;
import ru.kinoday.front.cinema.model.dto.ScheduleElementDto;
import ru.kinoday.front.cinema.model.dto.ServerStatus;
import ru.kinoday.front.news.service.NewsService;
import ru.kinoday.front.order.entity.Ticket;
import ru.kinoday.front.order.service.OrderService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Controller
@RequestMapping("/admin/")
public class AdminController {

    private OrderService orderService;
    private CinemaService cinemaService;
    private NewsService newsService;
    private ScheduleService scheduleService;

    @RequestMapping("/")
    public String index(
            Model m,
            @RequestParam(required = false) String hash,
            @RequestParam(required = false) Long ticketId
    ) {

        ServerStatus serverStatus = new ServerStatus();

        if (hash != null) {
            Ticket ticketByHash = orderService.getTicketByHash(hash);
            m.addAttribute("ticket", ticketByHash);
        }
        if (ticketId != null) {
            Ticket ticket = orderService.useTicket(ticketId);
            m.addAttribute("ticket", ticket);
        }

        MovieDto movie = new MovieDto();
        m.addAttribute("genres", Genre.values());
        m.addAttribute("movie", movie);
        NewsDto news = new NewsDto();
        m.addAttribute("news", news);

        ScheduleElementDto scheduleElementDto = new ScheduleElementDto();
        m.addAttribute("movies", cinemaService.getAllMovies());
        m.addAttribute("halls", getCinemaHallsNamesWithCinema());
        m.addAttribute("formats", Format.values());
        m.addAttribute("scheduleEl", scheduleElementDto);

        m.addAttribute("serverStatus", serverStatus);

        return "/admin/index";
    }

    private Map<Long, String> getCinemaHallsNamesWithCinema() {
        Map<Long, String> res = new HashMap<>();
        List<Cinema> allCinema = cinemaService.getAllCinema();
        for (Cinema cinema : allCinema) {
            for (CinemaHall cinemaHall : cinema.getCinemaHallList()) {
                res.put(cinemaHall.getId(),
                        cinema.getName() + ", " +
                                cinemaHall.getName() + " " +
                                cinemaHall.getAvailableFormats().
                                        stream().
                                        map(Format::valueOf).
                                        map(Format::getDescription).collect(Collectors.toList())
                );
            }
        }

        return res;
    }

    public String index(Model m) {
        return index(m, null, null);
    }

    @PostMapping("/movie")
    public String newMovie(
            @Valid @ModelAttribute MovieDto movie,
            Model model,
            BindingResult bindingResult
        ) {
        System.out.println(movie);
        if (bindingResult.hasErrors()){
            model.addAllAttributes(bindingResult.getAllErrors());
            return index(model);
        }

        if (cinemaService.addMovie(movie)) {
            model.addAttribute("success", "Вы успешно добавили фильм!");
        } else {
            model.addAttribute("error", "Ошибка при добавлении фильма");
        }

        return index(model);
    }

    @PostMapping("/schedule")
    public String newSchedule(
            @Valid @ModelAttribute ScheduleElementDto elementDto,
            Model model,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()){
            model.addAllAttributes(bindingResult.getAllErrors());
            return index(model);
        }

        ResponseEntity<String> res = scheduleService.addSchedule(elementDto);
        if (res.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("success", "Вы успешно добавили элемент в расписание!");
        } else {
            model.addAttribute("error", res.getBody());
        }
        return index(model);
    }

    @PostMapping("/news")
    public String newNews(
            @Valid @ModelAttribute NewsDto news,
            Model model,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()){
            model.addAllAttributes(bindingResult.getAllErrors());
            return index(model);
        }
        if (newsService.addNews(news)) {
            model.addAttribute("success", "Вы успешно добавили новость!");
        } else {
            model.addAttribute("error", "Ошибка при добавлении новости");
        }
        return index(model);
    }

    @GetMapping("/news/{id}")
    public String removeNews(@PathVariable Long id){
        newsService.deleteNews(id);
        return "redirect:/news/";
    }


    @GetMapping("/movie/{id}")
    public String removeMovie(@PathVariable Long id){
        System.out.println(id);
        cinemaService.deleteMovie(id);
        return "redirect:/movie/";
    }

    @GetMapping("/schedule/{id}")
    public String removeSchedule(@PathVariable Long id){
        scheduleService.deleteSchedule(id);
        return "redirect:/schedule/";
    }


}
