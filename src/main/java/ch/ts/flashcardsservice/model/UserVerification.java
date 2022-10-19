package ch.ts.flashcardsservice.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "user_verifications")
public class UserVerification {
    @Id
    private String id;
    @Column
    private String email;
    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;
}
