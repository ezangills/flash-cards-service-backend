package ch.ts.flashcardsservice.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SignupRequest {
    private String username;
    private String email;
    private String password;
}
