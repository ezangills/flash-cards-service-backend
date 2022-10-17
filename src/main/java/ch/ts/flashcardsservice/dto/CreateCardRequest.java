package ch.ts.flashcardsservice.dto;

import ch.ts.flashcardsservice.model.State;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateCardRequest {
    private String face;
    private String back;
    private String info;
    private State state;
}
