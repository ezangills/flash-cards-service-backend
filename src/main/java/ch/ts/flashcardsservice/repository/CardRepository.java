package ch.ts.flashcardsservice.repository;

import ch.ts.flashcardsservice.model.Card;
import ch.ts.flashcardsservice.model.Deck;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends CrudRepository<Card, Long> {
    List<Card> findAllByDeck(Deck deck);
    Optional<Card> findByIdAndDeck(Long id, Deck deck);
}
