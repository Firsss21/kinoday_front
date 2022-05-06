package ru.kinoday.front.cinema;

import ru.kinoday.front.cinema.model.Schedule;
import ru.kinoday.front.cinema.model.ScheduleDTO;
import ru.kinoday.front.cinema.model.Show;

public interface ScheduleService {

    Schedule getScheduleToday(Long cinemaId);

    Schedule getScheduleTomorrow(Long cinemaId);

    Schedule getScheduleForAnotherDay(Long cinemaId, int plusDays);

    Schedule getSchedule();

    Show getShow(long scheduleId);
}
