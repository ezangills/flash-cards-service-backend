package ch.ts.flashcardsservice.controller;

import ch.ts.flashcardsservice.dto.*;
import ch.ts.flashcardsservice.service.AuthService;
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
        return authService.refreshToken(request);
    }

    @PostMapping("/verification-request")
    public MessageResponse verificationRequest(@Valid @RequestBody VerificationRequest request) {
        return authService.createVerificationRequest(request.getEmail());
    }

    @PostMapping("/verify")
    public MessageResponse verify(@Valid @RequestBody VerificationFinishRequest request) {
        return authService.verify(request);
    }
}
