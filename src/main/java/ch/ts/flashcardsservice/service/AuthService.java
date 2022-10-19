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
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserVerificationRepository userVerificationRepository;
    private final EmailService emailService;

    @Value("${jwt.refresh-expiration-days}")
    private Long refreshTokenDurationMs;

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
                createRefreshToken(loginRequest.getUsername()));
    }

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

    @Transactional
    public MessageResponse signup(SignupRequest signUpRequest) {
        validate(signUpRequest);
        userRepository.save(new User()
                .setUsername(signUpRequest.getUsername())
                .setIsVerified(false)
                .setEmail(signUpRequest.getEmail())
                .setPassword(encoder.encode(signUpRequest.getPassword())));
        verificationRequest(signUpRequest.getEmail());
        return new MessageResponse().setMessage("User registered successfully! Verify your account by entering the code sent to your email.");
    }

    private void validate(SignupRequest signUpRequest) {
        validateExistence(signUpRequest);
        validateEmail(signUpRequest);
        validatePassword(signUpRequest);
        validateUsername(signUpRequest);
    }

    private void validateExistence(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new ServiceException("Username is already registered", HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new ServiceException("Email is already registered", HttpStatus.BAD_REQUEST);
        }
    }

    private void validateEmail(SignupRequest signupRequest) {
        boolean validEmail = Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
                .matcher(signupRequest.getEmail()).matches();
        if (!validEmail) {
            throw new ServiceException("Email is invalid", HttpStatus.BAD_REQUEST);
        }
    }

    private void validatePassword(SignupRequest signupRequest) {
        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasNumber = false;
        for (int i = 0; i < signupRequest.getPassword().length(); i++) {
            if (Character.isUpperCase(signupRequest.getPassword().charAt(i))) {
                hasUpperCase = true;
            }
            if (Character.isLowerCase(signupRequest.getPassword().charAt(i))) {
                hasLowerCase = true;
            }
            if (Character.isDigit(signupRequest.getPassword().charAt(i))) {
                hasNumber = true;
            }
        }
        if (!hasUpperCase || !hasLowerCase || !hasNumber || signupRequest.getPassword().length() < 8 || signupRequest.getPassword().length() > 20) {
            throw new ServiceException("Password is invalid. Password length should be between 8 and 20 characters. It should contain at least 1 digit, 1 lower case character and 1 upper case character.", HttpStatus.BAD_REQUEST);
        }
    }

    private void validateUsername(SignupRequest signupRequest) {
        boolean validUsername = Pattern.compile("^[A-Za-z]\\w{6,30}$")
                .matcher(signupRequest.getUsername()).matches();
        if (!validUsername) {
            throw new ServiceException("Username is invalid. It should start with alphabetic character and only contain alphanumerics and _. Username length should be between 6 and 30 characters", HttpStatus.BAD_REQUEST);
        }
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

    public MessageResponse createVerificationRequest(String email) {
        deleteExistingVerificationRequest(email);
        return verificationRequest(email);
    }

    @Transactional
    public void deleteExistingVerificationRequest(String email) {
        userVerificationRepository.findUserVerificationByEmail(email)
                .ifPresent(x -> userVerificationRepository.deleteById(x.getId()));
    }

    @Transactional
    public MessageResponse verificationRequest(String email) {
        String code = UUID.randomUUID().toString().substring(0, 6);
        userVerificationRepository.save(new UserVerification()
                .setEmail(email)
                .setExpiryDate(LocalDateTime.now().plusMinutes(15))
                .setId(code));
        emailService.sendVerificationEmail(email, code);
        return new MessageResponse().setMessage("Code has been sent to your email.");
    }

    @Transactional
    public MessageResponse verify(VerificationFinishRequest request) {
        var verification = userVerificationRepository.findUserVerificationByEmailAndId(request.getEmail(), request.getCode());
        if (verification.isPresent()) {
            userVerificationRepository.deleteById(verification.get().getId());
            userRepository.save(userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new ServiceException("User was not found", HttpStatus.INTERNAL_SERVER_ERROR))
                    .setIsVerified(true));
        }
        return new MessageResponse().setMessage("User has been verified successfully!");
    }
}
