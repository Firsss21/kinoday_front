package ru.kinoday.front.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kinoday.front.common.exception.UserAlreadyExistException;
import ru.kinoday.front.common.model.Role;
import ru.kinoday.front.common.model.User;
import ru.kinoday.front.common.service.UserService;
import ru.kinoday.front.common.validation.dto.ProfileDTO;
import ru.kinoday.front.common.validation.dto.UserDTO;
import ru.kinoday.front.order.entity.Ticket;
import ru.kinoday.front.order.entity.TicketType;
import ru.kinoday.front.order.service.OrderService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("/profile")
public class ProfileController {

    private OrderService orderService;
    private UserService userService;

    @GetMapping
    public String index(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        List<Ticket> tickets = orderService.getTicketByEmail(user.getEmail());

        model.addAttribute("purchasedType", TicketType.PURCHASED);
        model.addAttribute("bookedType", TicketType.BOOKED);
        model.addAttribute("usedType", TicketType.USED);

        model.addAttribute("user", user.getProfileDTO());
        model.addAttribute("tickets", tickets);
        return "user/profile";
    }

    @GetMapping("/buy")
    public String getPaymentLink(HttpSession session) {
        User user = (User) session.getAttribute("user");
        List<Ticket> tickets = orderService.getTicketByEmail(user.getEmail());
        return orderService.getPaymentLink(tickets, user.getId());
    }

    @PostMapping
    public String updateProfile(@ModelAttribute("user") @Valid ProfileDTO profileDTO, BindingResult result, Model model, HttpSession session) {

        if (result.hasErrors()) {
            model.addAllAttributes(result.getAllErrors());
            return "user/profile";
        }

        User user = (User) session.getAttribute("user");

        if (!profileDTO.getEmail().equals(user.getEmail())) {
            if (orderService.getTicketByEmail(user.getEmail()).size() > 0) {
                model.addAttribute("error", "Сейчас вы не можете поменять почтовый адрес");
                return "user/profile";
            }
            if (userService.findUserByEmail(profileDTO.getEmail()) != null) {
                model.addAttribute("error", "Пользователь с указаной почтой уже существует");
                return "user/profile";
            }
        }
        if ((!profileDTO.getPassword().isEmpty() || !profileDTO.getMatchingPassword().isEmpty()) && !profileDTO.getPassword().equals(profileDTO.getMatchingPassword())) {
            model.addAttribute("error", "Сейчас вы не можете поменять почтовый адрес");
            return "user/profile";
        }

        userService.updateUser(user.getId(), profileDTO);
        model.addAttribute("success", "Вы обновили профиль!");
        return "user/profile";
    }
}
