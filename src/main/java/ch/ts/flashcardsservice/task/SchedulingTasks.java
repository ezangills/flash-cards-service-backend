package ch.ts.flashcardsservice.task;

import ch.ts.flashcardsservice.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class SchedulingTasks {
    private final RefreshTokenRepository refreshTokenRepository;

    @Scheduled(cron = "0 0 * * * *")
    public void clearRefreshTokens() {
        refreshTokenRepository.deleteAllByExpiryDateBefore(LocalDateTime.now());
    }
}
