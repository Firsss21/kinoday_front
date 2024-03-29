package ru.kinoday.front.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kinoday.front.cinema.CinemaService;
import ru.kinoday.front.cinema.model.Movie;
import ru.kinoday.front.common.exception.UserAlreadyExistException;
import ru.kinoday.front.common.exception.UserNotFoundException;
import ru.kinoday.front.common.model.User;
import ru.kinoday.front.common.service.TokenService;
import ru.kinoday.front.common.service.UserService;
import ru.kinoday.front.common.validation.dto.UserDTO;
import ru.kinoday.front.news.service.NewsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Controller
@AllArgsConstructor
public class IndexController {

    private UserDetailsService userDetailsService;
    private UserService userService;
    private TokenService tokenService;
    private NewsService newsService;
    private CinemaService cinemaService;

    @GetMapping
    public String index(Model m) {
        m.addAttribute("news", newsService.getLastNews(3));
        m.addAttribute("movies", cinemaService.getLastMovies(6));
        m.addAttribute("cinema", cinemaService.getAllCinema());
        return "index";
    }
    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) {

        HttpSession session = request.getSession(false);
        String errorMessage = null;
        if (session != null) {
            AuthenticationException ex = (AuthenticationException) session
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ex != null) {
                errorMessage = ex.getMessage();
            }
        }
        model.addAttribute("errorMessage", errorMessage);
        return "login/signin";
    }

    @GetMapping("/success")
    public String success() {
        return "login/successfullyRegistered";
    }

    @GetMapping("/signup")
    public String registration(Model m) {
        UserDTO userDto = new UserDTO();
        m.addAttribute("user", userDto);
        return "login/signup";
    }

    @GetMapping("/changePassword")
    public String changePassword(@Valid @NotNull @RequestParam("token") String token, Model model){
        if (token == null) {
            return "redirect:/login";
        }
        String result = tokenService.validatePasswordResetToken(token);
        if(result != null) {
            return "redirect:/login";
        } else {
            model.addAttribute("token", token);
            return "login/resetPassword";
        }
    }

    @PostMapping("/signup")
    public String processRegistration(@ModelAttribute("user") @Valid UserDTO userDto, BindingResult result, Model model)
    {
        if (result.hasErrors()) {
            model.addAllAttributes(result.getAllErrors());
            return "login/signup";
        }

        try {
            User registered = userService.registerNewUserAccount(userDto);
        } catch (UserAlreadyExistException uaeEx) {
            model.addAttribute("message", "Пользователь с указаной почтой уже существует");
            return "login/signup";
        }
        return "redirect:/success";
    }

    @GetMapping("/forgotPassword")
    public String forgotPassword() {
        return "login/forgotPassword";
    }

    @PostMapping("/forgotPassword")
    public String processForgetPassword(@NotNull @RequestParam("email") String userEmail, Model model) {
        try {
            userService.generateToken(userEmail);
        }  catch (UserNotFoundException e) {
            model.addAttribute("message", "Пользователь с введенной почтой не найден.");
            return "login/forgotPassword";
        }
        model.addAttribute("message", "Проверьте вашу почту!");
        return "login/forgotPassword";
    }

    @PostMapping("/changePassword")
    public String processChangePassword(@RequestParam("token") String token, @Valid @Min(5) @NotNull @RequestParam("password") String password, Model model){
        String result = tokenService.validatePasswordResetToken(token);
        if(result != null) {
            return "redirect:/login";
        } else {
            User user;
            try {
                user = tokenService.getUserByToken(token);
            } catch (UserNotFoundException e) {
                return "redirect:/login";
            }
            userService.updatePassword(user, password);
            return "redirect:/login";

        }
    }

    @GetMapping("/rules")
    public String getRules() {
        return "info/rules";
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Boolean> userExist(@PathVariable long id){
        boolean b = userService.userExist(id);
        return ResponseEntity.ok(b);
    }

    @GetMapping("/error")
    public String handleError() {
        return "error";
    }

}
