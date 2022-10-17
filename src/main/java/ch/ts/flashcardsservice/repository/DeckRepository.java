package ch.ts.flashcardsservice.repository;

import ch.ts.flashcardsservice.model.Deck;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeckRepository extends CrudRepository<Deck, Long> {
    List<Deck> findAllByUsername(String username);
}
