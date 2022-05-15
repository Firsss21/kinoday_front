package ru.kinoday.front.news.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.kinoday.front.news.entity.News;
import ru.kinoday.front.news.entity.NewsList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class NewsService {

    @Value("${service.news.uri}")
    private String hostAddress;

    private final String path = "/news/";

    @Autowired
    RestTemplate restTemplate;

    public List<News> getAllNews() {

        try {
            ResponseEntity<News[]> response = restTemplate.getForEntity(hostAddress + path, News[].class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return Arrays.stream(response.getBody()).collect(Collectors.toList());
            } else {
                return new ArrayList<>();
            }
        } catch (RestClientException e) {
            e.printStackTrace();
        } finally {
            return new ArrayList<>();
        }
    }

    public List<News> getLastNews(int count) {
        try {
            ResponseEntity<News[]> response = restTemplate.getForEntity(hostAddress + path + "last/?count=" + count, News[].class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return Arrays.stream(response.getBody()).collect(Collectors.toList());
            } else {
                return new ArrayList<>();
            }
        } catch (RestClientException e) {
            e.printStackTrace();
        } finally {
            return new ArrayList<>();
        }
    }
}
