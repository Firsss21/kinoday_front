package ru.kinoday.front.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kinoday.front.common.exception.SendEmailException;
import ru.kinoday.front.common.exception.UserAlreadyExistException;
import ru.kinoday.front.common.exception.UserNotFoundException;
import ru.kinoday.front.common.model.User;
import ru.kinoday.front.common.service.TokenService;
import ru.kinoday.front.common.service.UserService;
import ru.kinoday.front.common.validation.dto.UserDTO;
import ru.kinoday.front.news.service.NewsService;

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

    @GetMapping
    public String index(Model m) {
        m.addAttribute("news", newsService.getLastNews(3));
        return "index";
    }
    @GetMapping("/login")
    public String login() {
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

}
