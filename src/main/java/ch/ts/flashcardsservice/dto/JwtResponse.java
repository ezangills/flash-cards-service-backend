package ch.ts.flashcardsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class JwtResponse {
    private String token;
    private String username;
    private String email;
    private String refreshToken;
}
