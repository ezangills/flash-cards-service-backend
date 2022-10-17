package ch.ts.flashcardsservice.dto;

import ch.ts.flashcardsservice.model.State;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateDeckRequest {
    private String name;
    private State state;
}
