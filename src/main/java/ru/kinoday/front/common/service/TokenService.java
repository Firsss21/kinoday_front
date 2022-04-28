package ru.kinoday.front.common.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kinoday.front.common.exception.UserNotFoundException;
import ru.kinoday.front.common.model.PasswordResetToken;
import ru.kinoday.front.common.model.User;
import ru.kinoday.front.common.repository.PasswordResetTokenRepository;

import java.util.Calendar;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TokenService {

    PasswordResetTokenRepository tokenRepo;

    public String validatePasswordResetToken(String token) {
        final Optional<PasswordResetToken> passToken = tokenRepo.findByToken(token);

        return !isTokenFound(passToken) ? "invalidToken"
                : isTokenExpired(passToken) ? "expired"
                : null;
    }

    private boolean isTokenFound(Optional<PasswordResetToken> passToken) {
        return passToken.isPresent();
    }

    private boolean isTokenExpired(Optional<PasswordResetToken> passToken) {
        final Calendar cal = Calendar.getInstance();
        return passToken.get().getExpiryDate().before(cal.getTime());
    }

    public User getUserByToken(String token) throws UserNotFoundException {
        final PasswordResetToken passToken = tokenRepo.findByToken(token).get();

        if (passToken == null) {
            throw new UserNotFoundException("User with this token not found");
        }

        return passToken.getUser();
    }
}
