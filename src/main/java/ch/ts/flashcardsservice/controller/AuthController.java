package ch.ts.flashcardsservice.controller;

import ch.ts.flashcardsservice.dto.JwtResponse;
import ch.ts.flashcardsservice.dto.LoginRequest;
import ch.ts.flashcardsservice.dto.MessageResponse;
import ch.ts.flashcardsservice.dto.SignupRequest;
import ch.ts.flashcardsservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/*@RestController
@RequestMapping("/api/auth")*/
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signin")
    public JwtResponse signin(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.signIn(loginRequest);
    }

    @PostMapping("/signup")
    public MessageResponse signup(@Valid @RequestBody SignupRequest signUpRequest) {
        return authService.signup(signUpRequest);
    }
}
