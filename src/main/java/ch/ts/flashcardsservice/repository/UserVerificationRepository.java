package ch.ts.flashcardsservice.repository;

import ch.ts.flashcardsservice.model.UserVerification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserVerificationRepository extends CrudRepository<UserVerification, String> {
    Optional<UserVerification> findUserVerificationByEmail(String email);
    Optional<UserVerification> findUserVerificationByEmailAndId(String email, String id);
}
