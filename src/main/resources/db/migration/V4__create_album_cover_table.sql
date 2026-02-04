CREATE TABLE album_cover (
    id BIGSERIAL PRIMARY KEY,
    album_id BIGINT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    minio_key VARCHAR(500) NOT NULL,
    content_type VARCHAR(100),
    file_size BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_album_cover_album FOREIGN KEY (album_id) REFERENCES album(id) ON DELETE CASCADE
);
