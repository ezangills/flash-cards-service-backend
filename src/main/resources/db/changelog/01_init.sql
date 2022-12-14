create table users
(
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(64) UNIQUE NOT NULL,
    email VARCHAR(64) UNIQUE NOT NULL,
    password VARCHAR(128) NOT NULL,
    is_verified BOOLEAN NOT NULL
);

create table user_verifications
(
    id VARCHAR(6) PRIMARY KEY,
    email VARCHAR(64) UNIQUE NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    CONSTRAINT fk_email_verification FOREIGN KEY(email) REFERENCES users(email)
);

create table refresh_tokens
(
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(64) NOT NULL,
    token VARCHAR(36) UNIQUE NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    CONSTRAINT fk_user_token FOREIGN KEY(username) REFERENCES users(username)
);

create table decks
(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    state VARCHAR(16) NOT NULL,
    username VARCHAR(64) NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY(username) REFERENCES users(username)
);

CREATE INDEX ix_decks_username ON decks (username);

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