package ch.ts.flashcardsservice.service;

import ch.ts.flashcardsservice.dto.*;
import ch.ts.flashcardsservice.exception.ServiceException;
import ch.ts.flashcardsservice.model.RefreshToken;
import ch.ts.flashcardsservice.model.User;
import ch.ts.flashcardsservice.model.UserDetailsImpl;
import ch.ts.flashcardsservice.model.UserVerification;
import ch.ts.flashcardsservice.repository.RefreshTokenRepository;
import ch.ts.flashcardsservice.repository.UserRepository;
import ch.ts.flashcardsservice.repository.UserVerificationRepository;
import ch.ts.flashcardsservice.util.JwtUtils;
import ch.ts.flashcardsservice.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final UserVerificationService userVerificationService;
    private final ValidationUtils validationUtils;
    private final RefreshTokenService refreshTokenService;

    public JwtResponse signIn(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        if (!userDetails.getIsVerified()) {
            throw new ServiceException("User's email is not verified!", HttpStatus.UNAUTHORIZED);
        }
        return new JwtResponse(
                jwt,
                userDetails.getUsername(),
                userDetails.getEmail(),
                refreshTokenService.createRefreshToken(loginRequest.getUsername()));
    }

    @Transactional
    public MessageResponse signup(SignupRequest signUpRequest) {
        validationUtils.validate(signUpRequest);
        userRepository.save(new User()
                .setUsername(signUpRequest.getUsername())
                .setIsVerified(false)
                .setEmail(signUpRequest.getEmail())
                .setPassword(encoder.encode(signUpRequest.getPassword())));
        userVerificationService.verificationRequest(signUpRequest.getEmail());
        return new MessageResponse().setMessage("User registered successfully! Verify your account by entering the code sent to your email.");
    }
}
