package ru.kinoday.front.order.entity;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.kinoday.front.cinema.model.Place;
import ru.kinoday.front.cinema.model.TicketPlace;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Min(value = 0, message = "Непредвиденная ошибка")
    private Long scheduleId;

    @Size(min=1, message="Выберите место")
    private List<Place> tickets = new ArrayList<>();

    @AssertTrue(message = "Для продолжения оформления билетов нужно согласиться с правилами")
    boolean agreementAccepted;
}
