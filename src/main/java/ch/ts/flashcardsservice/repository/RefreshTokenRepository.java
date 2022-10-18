package ch.ts.flashcardsservice.repository;

import ch.ts.flashcardsservice.model.RefreshToken;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    @Modifying
    void deleteByUsername(String username);
    @Modifying
    void deleteAllByExpiryDateBefore(LocalDateTime now);
}
