package ch.ts.flashcardsservice.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RefreshTokenRequest {
    private String refreshToken;
}
