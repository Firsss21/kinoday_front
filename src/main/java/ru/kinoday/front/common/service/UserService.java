package ru.kinoday.front.common.service;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kinoday.front.common.exception.UserAlreadyExistException;
import ru.kinoday.front.common.exception.UserNotFoundException;
import ru.kinoday.front.common.model.*;
import ru.kinoday.front.common.repository.PasswordResetTokenRepository;
import ru.kinoday.front.common.repository.UserRepository;
import ru.kinoday.front.common.validation.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    PasswordEncoder passwordEncoder;

    MailService emailService;

    UserRepository userRepository;

    PasswordResetTokenRepository tokenRepo;

    public User registerNewUserAccount(UserDTO userDto) throws UserAlreadyExistException {
        if (emailExist(userDto.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email address: "
                    + userDto.getEmail());
        }

        User user = new User();
        user.setLogin(userDto.getLogin());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);

        List<Permission> privileges = new ArrayList<>(user.getRole().getPermissions());
        List<GrantedAuthority> authorities = privileges.stream()
                .map(p -> new SimpleGrantedAuthority(p.getPermission()))
                .collect(Collectors.toList());
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return userRepository.save(user);
    }

    private boolean emailExist(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent();
    }

    public void generateToken(String userEmail) throws UserNotFoundException {
        if (!emailExist(userEmail)) {
            throw new UserNotFoundException("User with this email not found");
        }
        String token = UUID.randomUUID().toString();
        PasswordResetToken myToken = new PasswordResetToken(token, userRepository.findByEmail(userEmail).get());
        tokenRepo.save(myToken);

        String msg = "Hello! You can change your password with this link! " +
                "localhost:8084/changePassword?token=" + token;
        emailService.sendSimpleMessage(userEmail, "Password recovery", msg);
    }

    public void updatePassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
