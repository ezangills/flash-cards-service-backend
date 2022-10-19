package ch.ts.flashcardsservice.task;

import ch.ts.flashcardsservice.repository.RefreshTokenRepository;
import ch.ts.flashcardsservice.repository.UserVerificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class SchedulingTasks {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserVerificationRepository userVerificationRepository;

    @Scheduled(cron = "0 0 * * * *")
    public void clearExpiredData() {
        log.info("Started clearing process...");
        refreshTokenRepository.deleteAllByExpiryDateBefore(LocalDateTime.now());
        userVerificationRepository.deleteAllByExpiryDateBefore(LocalDateTime.now());
        log.info("Finished clearing process");
    }
}
