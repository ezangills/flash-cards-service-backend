package ch.ts.flashcardsservice.controller;

import ch.ts.flashcardsservice.dto.CreateDeckRequest;
import ch.ts.flashcardsservice.dto.DeckDto;
import ch.ts.flashcardsservice.dto.UpdateDeckRequest;
import ch.ts.flashcardsservice.service.DeckService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class DecksController {
    private final DeckService deckService;

    @PostMapping("/decks")
    public DeckDto createDeck(@RequestBody CreateDeckRequest deck) {
        return deckService.createDeck(deck);
    }

    @GetMapping("/decks")
    public List<DeckDto> getDecks() {
        return deckService.getDecks();
    }

    @PutMapping("/decks/{deckId}")
    public DeckDto updateDeck(@PathVariable Long deckId,
                              @RequestBody UpdateDeckRequest deckDto) {
        return deckService.updateDeck(deckId, deckDto);
    }

    @PatchMapping("/decks/{deckId}/{statusName}")
    public DeckDto patchDeckStatusName(@PathVariable Long deckId,
                                       @PathVariable String statusName) {
        return deckService.patchDeckStatusName(deckId, statusName);
    }

    @DeleteMapping("/decks/{deckId}")
    public void deleteDeck(@PathVariable Long deckId) {
        deckService.deleteDeck(deckId);
    }
}
