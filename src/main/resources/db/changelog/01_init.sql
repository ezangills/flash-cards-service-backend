create table decks
(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    state VARCHAR(16) NOT NULL,
    username VARCHAR(64) NOT NULL
);

create table cards
(
    id BIGSERIAL PRIMARY KEY,
    face TEXT,
    back TEXT,
    info TEXT,
    state VARCHAR(16) NOT NULL,
    deck_id BIGINT,
    CONSTRAINT fk_deck FOREIGN KEY(deck_id) REFERENCES decks(id)
);

CREATE INDEX ix_card_deck_id ON cards (deck_id);