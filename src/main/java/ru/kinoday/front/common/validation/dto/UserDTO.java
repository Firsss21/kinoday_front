package ru.kinoday.front.common.validation.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.kinoday.front.common.validation.PasswordMatches;
import ru.kinoday.front.common.validation.ValidEmail;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Data
@PasswordMatches
public class UserDTO {
    @NotNull
    @NotEmpty
    @Size(min = 3, max = 40)
    private String login;

    @NotNull
    @NotEmpty
    @Size(min = 8, max = 40)
    private String password;
    @NotNull
    @NotEmpty
    @Size(min = 8, max = 40)
    private String matchingPassword;

    @NotNull
    @NotEmpty
    @ValidEmail
    private String email;

    public UserDTO() {

    }
}
