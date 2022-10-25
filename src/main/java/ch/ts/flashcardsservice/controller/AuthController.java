package ch.ts.flashcardsservice.controller;

import ch.ts.flashcardsservice.dto.*;
import ch.ts.flashcardsservice.service.AuthService;
import ch.ts.flashcardsservice.service.RefreshTokenService;
import ch.ts.flashcardsservice.service.UserVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserVerificationService userVerificationService;
    private final RefreshTokenService refreshTokenService;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(value = "/signin")
    public JwtResponse signin(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.signIn(loginRequest);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/signup")
    public MessageResponse signup(@Valid @RequestBody SignupRequest signUpRequest) {
        return authService.signup(signUpRequest);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/refreshtoken")
    public JwtResponse refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return refreshTokenService.refreshToken(request);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/verification-request")
    public MessageResponse verificationRequest(@Valid @RequestBody VerificationRequest request) {
        return userVerificationService.createVerificationRequest(request.getEmail());
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/verify")
    public MessageResponse verify(@Valid @RequestBody VerificationFinishRequest request) {
        return userVerificationService.verify(request);
    }
}
