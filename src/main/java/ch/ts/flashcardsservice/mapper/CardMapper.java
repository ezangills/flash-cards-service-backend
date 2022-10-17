package ch.ts.flashcardsservice.mapper;

import ch.ts.flashcardsservice.dto.CardDto;
import ch.ts.flashcardsservice.dto.CreateCardRequest;
import ch.ts.flashcardsservice.dto.CreateDeckRequest;
import ch.ts.flashcardsservice.dto.DeckDto;
import ch.ts.flashcardsservice.model.Card;
import ch.ts.flashcardsservice.model.Deck;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardMapper {
    Card map(CardDto dto);
    Card map(CreateCardRequest dto);
    CardDto map(Card dto);
    Deck map(DeckDto dto);
    Deck map(CreateDeckRequest dto);
    DeckDto map(Deck dto);
}
