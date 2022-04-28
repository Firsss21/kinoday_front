package ru.kinoday.front.common.validation.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.kinoday.front.common.validation.PasswordMatches;
import ru.kinoday.front.common.validation.ValidEmail;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@Data
@PasswordMatches
public class UserDTO {
    @NotNull
    @NotEmpty
    private String login;

    @NotNull
    @NotEmpty
    private String password;
    @NotNull
    @NotEmpty
    private String matchingPassword;

    @NotNull
    @NotEmpty
    @ValidEmail
    private String email;

    public UserDTO() {

    }
}
