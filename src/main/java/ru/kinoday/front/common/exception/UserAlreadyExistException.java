package ru.kinoday.front.common.exception;

import javax.validation.constraints.NotEmpty;

public class UserAlreadyExistException extends Throwable {
    public UserAlreadyExistException(@NotEmpty String s) {
    }
}
