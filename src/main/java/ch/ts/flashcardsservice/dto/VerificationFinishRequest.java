package ch.ts.flashcardsservice.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class VerificationFinishRequest {
    private String code;
    private String email;
}
