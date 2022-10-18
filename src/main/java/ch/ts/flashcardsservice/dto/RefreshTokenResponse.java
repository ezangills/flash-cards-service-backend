package ch.ts.flashcardsservice.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RefreshTokenResponse {
    private String token;
    private String refreshToken;
}
