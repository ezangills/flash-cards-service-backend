package ch.ts.flashcardsservice.controller;

import ch.ts.flashcardsservice.dto.CardDto;
import ch.ts.flashcardsservice.dto.CreateCardRequest;
import ch.ts.flashcardsservice.dto.UpdateCardRequest;
import ch.ts.flashcardsservice.model.State;
import ch.ts.flashcardsservice.service.CardService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class CardsController {
    private final CardService cardService;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/decks/{deckId}/cards")
    public CardDto createCard(@PathVariable Long deckId,
                              @RequestBody CreateCardRequest cardDto) {
        return cardService.createCard(deckId, cardDto);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/decks/{deckId}/cards/")
    public List<CardDto> getCardsByDeck(@PathVariable Long deckId,
                                        @RequestParam(defaultValue = "") Set<State> filterStates) {
        return cardService.getCardsByDeck(deckId, filterStates);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/decks/{deckId}/cards/{cardId}")
    public CardDto updateCard(@PathVariable Long deckId,
                              @PathVariable Long cardId,
                              @RequestBody UpdateCardRequest cardDto) {
        return cardService.updateCard(deckId, cardId, cardDto);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PatchMapping("/decks/{deckId}/cards/{cardId}/status/{statusName}")
    public CardDto patchCardStatusName(@PathVariable Long deckId,
                              @PathVariable Long cardId,
                              @PathVariable String statusName) {
        return cardService.patchCardStatusName(deckId, cardId, statusName);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/decks/{deckId}/cards/{cardId}")
    public void deleteCard(@PathVariable Long deckId,
                              @PathVariable Long cardId) {
        cardService.deleteCard(deckId, cardId);
    }
}
