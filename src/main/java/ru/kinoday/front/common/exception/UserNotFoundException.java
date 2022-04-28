package ru.kinoday.front.common.exception;


import javax.validation.constraints.NotEmpty;

public class UserNotFoundException extends Throwable {
    public UserNotFoundException(@NotEmpty String s) {
    }
}
