package ru.kinoday.front.cinema.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NewsDto {

    @Size(min = 1, max = 150)
    private String name;

    @Size(min = 1, max = 4000)
    private String text;
}
