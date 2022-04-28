package ru.kinoday.front.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kinoday.front.common.model.PasswordResetToken;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
}
