package ru.kinoday.front.news.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kinoday.front.news.service.NewsService;

@Controller
@RequestMapping("/news/")
@AllArgsConstructor
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping
    public String indexNews(Model m) {
        m.addAttribute(newsService.getAllNews());
        return "news/feed";
    }

}
