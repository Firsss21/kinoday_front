package ru.kinoday.front.common.model;

import jdk.jfr.DataAmount;
import lombok.Data;
import ru.kinoday.front.common.validation.dto.ProfileDTO;
import ru.kinoday.front.common.validation.dto.UserDTO;

import javax.persistence.*;

@Data
@Entity
@Table(name = "KinoUser")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "login")
    private String login;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private Role role;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private Status status;

    private String ip;

    public UserDTO getDTO() {
        return new UserDTO(login, password, password, getEmail());
    }

    public ProfileDTO getProfileDTO() {
        return new ProfileDTO(id, login, password, password, email, role == Role.ADMIN);
    }
}
