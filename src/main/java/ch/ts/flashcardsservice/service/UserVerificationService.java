package ch.ts.flashcardsservice.service;

import ch.ts.flashcardsservice.dto.MessageResponse;
import ch.ts.flashcardsservice.dto.VerificationFinishRequest;
import ch.ts.flashcardsservice.exception.ServiceException;
import ch.ts.flashcardsservice.model.UserVerification;
import ch.ts.flashcardsservice.repository.UserRepository;
import ch.ts.flashcardsservice.repository.UserVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserVerificationService {
    private final UserVerificationRepository userVerificationRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

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
        } else {
            throw new ServiceException("Couldn't find the code for the user with such email", HttpStatus.BAD_REQUEST);
        }
        return new MessageResponse().setMessage("User has been verified successfully!");
    }
}
