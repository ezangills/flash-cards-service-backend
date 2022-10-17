package ch.ts.flashcardsservice.service;

import ch.ts.flashcardsservice.dto.CreateDeckRequest;
import ch.ts.flashcardsservice.dto.DeckDto;
import ch.ts.flashcardsservice.dto.UpdateDeckRequest;
import ch.ts.flashcardsservice.exception.ServiceException;
import ch.ts.flashcardsservice.mapper.CardMapper;
import ch.ts.flashcardsservice.model.Card;
import ch.ts.flashcardsservice.model.Deck;
import ch.ts.flashcardsservice.model.State;
import ch.ts.flashcardsservice.repository.CardRepository;
import ch.ts.flashcardsservice.repository.DeckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeckService {
    private final DeckRepository deckRepository;
    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    @Transactional
    public DeckDto createDeck(CreateDeckRequest deck) {
        return cardMapper.map(deckRepository.save(cardMapper.map(deck)));
    }

    @Transactional
    public List<DeckDto> getDecks(String username) {
        return deckRepository.findAllByUsername(username)
                .stream()
                .map(cardMapper::map)
                .toList();
    }

    @Transactional
    public DeckDto updateDeck(Long deckId, UpdateDeckRequest deckDto) {
        return cardMapper.map(deckRepository.save(findDeckById(deckId)
                .setState(deckDto.getState())
                .setName(deckDto.getName())));
    }

    @Transactional
    public DeckDto patchDeckStatusName(Long deckId, String statusName) {
        return cardMapper.map(deckRepository.save(findDeckById(deckId)
                .setState(State.valueOf(statusName))));
    }

    @Transactional
    public void deleteDeck(Long deckId) {
        var deck = findDeckById(deckId);
        cardRepository.deleteAllById(deck.getCards().stream().map(Card::getId).toList());
        deckRepository.delete(deck);
    }

    @Transactional
    public Deck findDeckById(Long deckId) {
        return deckRepository.findById(deckId).orElseThrow(() -> new ServiceException("Deck with id " + deckId + " was not found", HttpStatus.BAD_REQUEST));
    }
}
