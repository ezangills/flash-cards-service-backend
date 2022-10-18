package ch.ts.flashcardsservice.service;

import ch.ts.flashcardsservice.dto.JwtResponse;
import ch.ts.flashcardsservice.dto.LoginRequest;
import ch.ts.flashcardsservice.dto.MessageResponse;
import ch.ts.flashcardsservice.dto.SignupRequest;
import ch.ts.flashcardsservice.exception.ServiceException;
import ch.ts.flashcardsservice.model.User;
import ch.ts.flashcardsservice.model.UserDetailsImpl;
import ch.ts.flashcardsservice.repository.UserRepository;
import ch.ts.flashcardsservice.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    public JwtResponse signIn(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return new JwtResponse(
                jwt,
                userDetails.getUsername(),
                userDetails.getEmail());
    }

    public MessageResponse signup(SignupRequest signUpRequest) {
        validate(signUpRequest);
        userRepository.save(new User()
                .setUsername(signUpRequest.getUsername())
                .setEmail(signUpRequest.getEmail())
                .setPassword(encoder.encode(signUpRequest.getPassword())));
        return new MessageResponse().setMessage("User registered successfully!");
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
        boolean validPassword = Pattern.compile("“^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\\\S+$).{8,20}$”")
                .matcher(signupRequest.getPassword()).matches();
        if (!validPassword) {
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
}
