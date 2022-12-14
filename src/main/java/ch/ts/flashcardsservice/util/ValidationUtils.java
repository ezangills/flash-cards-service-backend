package ch.ts.flashcardsservice.util;

import ch.ts.flashcardsservice.dto.SignupRequest;
import ch.ts.flashcardsservice.exception.ServiceException;
import ch.ts.flashcardsservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class ValidationUtils {
    private final UserRepository userRepository;

    public void validate(SignupRequest signUpRequest) {
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
}
