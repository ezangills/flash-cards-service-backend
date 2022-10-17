package ch.ts.flashcardsservice.service;

import ch.ts.flashcardsservice.dto.CardDto;
import ch.ts.flashcardsservice.dto.CreateCardRequest;
import ch.ts.flashcardsservice.dto.UpdateCardRequest;
import ch.ts.flashcardsservice.exception.ServiceException;
import ch.ts.flashcardsservice.mapper.CardMapper;
import ch.ts.flashcardsservice.model.Card;
import ch.ts.flashcardsservice.model.State;
import ch.ts.flashcardsservice.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final DeckService deckService;
    private final CardMapper cardMapper;

    @Transactional
    public CardDto createCard(Long deckId, CreateCardRequest cardDto) {
        return cardMapper.map(cardRepository.save(cardMapper.map(cardDto).setDeck(deckService.findDeckById(deckId))));
    }

    @Transactional
    public List<CardDto> getCardsByDeck(Long deckId, Set<State> filterStates) {
        return cardRepository.findAllByDeck(deckService.findDeckById(deckId))
                .stream()
                .filter(x -> filterStates.contains(x.getState()))
                .map(cardMapper::map)
                .toList();
    }

    @Transactional
    public CardDto updateCard(Long deckId, Long cardId, UpdateCardRequest cardDto) {
        return cardMapper.map(cardRepository.save(findByIdAndDeck(cardId, deckId)
                .setState(cardDto.getState())
                .setBack(cardDto.getBack())
                .setFace(cardDto.getFace())
                .setInfo(cardDto.getInfo())));
    }

    @Transactional
    public CardDto patchCardStatusName(Long deckId, Long cardId, String statusName) {
        return cardMapper.map(cardRepository.save(findByIdAndDeck(cardId, deckId)
                .setState(State.valueOf(statusName))));
    }

    @Transactional
    public void deleteCard(Long deckId, Long cardId) {
        cardRepository.deleteById(findByIdAndDeck(cardId, deckId).getId());
    }

    @Transactional
    public Card findByIdAndDeck(Long cardId, Long deckId) {
        return cardRepository.findByIdAndDeck(cardId, deckService.findDeckById(deckId))
                .orElseThrow(() -> new ServiceException("Card with id " + cardId + " and deck id " + deckId + " is not found", HttpStatus.BAD_REQUEST));
    }
}
