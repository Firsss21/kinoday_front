package ru.kinoday.front.common.validation.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.kinoday.front.common.validation.PasswordMatches;
import ru.kinoday.front.common.validation.ValidEmail;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class ProfileDTO {

        private Long uid;
        @NotNull
        @NotEmpty
        @Size(min = 3, max = 40, message = "Укажите размер от 3 до 40 символов")
        private String login;

        private String password;
        private String matchingPassword;

        @NotNull
        @NotEmpty
        @ValidEmail(message = "Введите корректную почту")
        private String email;

        private boolean isAdmin;

}
