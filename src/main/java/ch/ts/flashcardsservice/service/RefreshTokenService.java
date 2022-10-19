package ch.ts.flashcardsservice.service;

import ch.ts.flashcardsservice.dto.JwtResponse;
import ch.ts.flashcardsservice.dto.RefreshTokenRequest;
import ch.ts.flashcardsservice.exception.ServiceException;
import ch.ts.flashcardsservice.model.RefreshToken;
import ch.ts.flashcardsservice.repository.RefreshTokenRepository;
import ch.ts.flashcardsservice.repository.UserRepository;
import ch.ts.flashcardsservice.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    @Value("${jwt.refresh-expiration-days}")
    private Long refreshTokenDurationMs;

    public String createRefreshToken(String username) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsername(username);
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken.getToken();
    }

    public RefreshToken findRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ServiceException("Refresh token was expired", HttpStatus.BAD_REQUEST));
    }

    public RefreshToken verifyRefreshTokenExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(token);
            throw new ServiceException("Refresh token was expired", HttpStatus.BAD_REQUEST);
        }
        return token;
    }

    public JwtResponse refreshToken(RefreshTokenRequest request) {
        var refreshToken = verifyRefreshTokenExpiration(findRefreshToken(request.getRefreshToken()));
        String jwt = jwtUtils.generateTokenFromUsername(refreshToken.getUsername());
        return new JwtResponse(
                jwt,
                refreshToken.getUsername(),
                userRepository.findByUsername(refreshToken.getUsername()).orElseThrow(() -> new ServiceException("Couldn't refresh token", HttpStatus.INTERNAL_SERVER_ERROR)).getEmail(),
                createRefreshToken(refreshToken.getUsername()));
    }
}
