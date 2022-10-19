package ch.ts.flashcardsservice.controller;

import ch.ts.flashcardsservice.dto.*;
import ch.ts.flashcardsservice.service.AuthService;
import ch.ts.flashcardsservice.service.RefreshTokenService;
import ch.ts.flashcardsservice.service.UserVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserVerificationService userVerificationService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping(value = "/signin")
    public JwtResponse signin(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.signIn(loginRequest);
    }

    @PostMapping("/signup")
    public MessageResponse signup(@Valid @RequestBody SignupRequest signUpRequest) {
        return authService.signup(signUpRequest);
    }

    @PostMapping("/refreshtoken")
    public JwtResponse refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return refreshTokenService.refreshToken(request);
    }

    @PostMapping("/verification-request")
    public MessageResponse verificationRequest(@Valid @RequestBody VerificationRequest request) {
        return userVerificationService.createVerificationRequest(request.getEmail());
    }

    @PostMapping("/verify")
    public MessageResponse verify(@Valid @RequestBody VerificationFinishRequest request) {
        return userVerificationService.verify(request);
    }
}
