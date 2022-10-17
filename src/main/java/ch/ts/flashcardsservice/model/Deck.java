package ch.ts.flashcardsservice.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "decks")
public class Deck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @OneToMany(mappedBy = "deck", fetch = FetchType.EAGER)
    private List<Card> cards = new ArrayList<>();
    @Column
    @Enumerated(EnumType.STRING)
    private State state;
    @Column
    private String username;
}
