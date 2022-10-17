package ch.ts.flashcardsservice.controller;

import ch.ts.flashcardsservice.dto.CardDto;
import ch.ts.flashcardsservice.dto.CreateCardRequest;
import ch.ts.flashcardsservice.dto.UpdateCardRequest;
import ch.ts.flashcardsservice.model.State;
import ch.ts.flashcardsservice.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CardsController {
    private final CardService cardService;

    @PostMapping("/decks/{deckId}/cards")
    public CardDto createCard(@PathVariable Long deckId,
                              @RequestBody CreateCardRequest cardDto) {
        return cardService.createCard(deckId, cardDto);
    }

    @GetMapping("/decks/{deckId}/cards/")
    public List<CardDto> getCardsByDeck(@PathVariable Long deckId,
                                        @RequestParam(defaultValue = "") Set<State> filterStates) {
        return cardService.getCardsByDeck(deckId, filterStates);
    }

    @PutMapping("/decks/{deckId}/cards/{cardId}")
    public CardDto updateCard(@PathVariable Long deckId,
                              @PathVariable Long cardId,
                              @RequestBody UpdateCardRequest cardDto) {
        return cardService.updateCard(deckId, cardId, cardDto);
    }

    @PatchMapping("/decks/{deckId}/cards/{cardId}/status/{statusName}")
    public CardDto patchCardStatusName(@PathVariable Long deckId,
                              @PathVariable Long cardId,
                              @PathVariable String statusName) {
        return cardService.patchCardStatusName(deckId, cardId, statusName);
    }

    @DeleteMapping("/decks/{deckId}/cards/{cardId}")
    public void deleteCard(@PathVariable Long deckId,
                              @PathVariable Long cardId) {
        cardService.deleteCard(deckId, cardId);
    }
}
