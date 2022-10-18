package ch.ts.flashcardsservice.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String username;
    private String email;
}
