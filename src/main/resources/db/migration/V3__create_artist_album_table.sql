-- V3: Create artist_album junction table (N:N relationship)
CREATE TABLE artist_album (
    artist_id BIGINT NOT NULL,
    album_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (artist_id, album_id),
    CONSTRAINT fk_artist_album_artist FOREIGN KEY (artist_id) REFERENCES artist(id) ON DELETE CASCADE,
    CONSTRAINT fk_artist_album_album FOREIGN KEY (album_id) REFERENCES album(id) ON DELETE CASCADE
);