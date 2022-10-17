package ch.ts.flashcardsservice.dto;

import ch.ts.flashcardsservice.model.State;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class DeckDto {
    private Long id;
    private String name;
    private List<CardDto> cards;
    private State state;
    private String username;
}
