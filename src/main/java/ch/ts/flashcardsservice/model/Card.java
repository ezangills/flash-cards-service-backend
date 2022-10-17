package ch.ts.flashcardsservice.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String face;
    @Column
    private String back;
    @Column
    private String info;
    @ManyToOne
    @JoinColumn(name="deck_id", referencedColumnName = "id", nullable=false)
    private Deck deck;
    @Column
    @Enumerated(EnumType.STRING)
    private State state;

}
