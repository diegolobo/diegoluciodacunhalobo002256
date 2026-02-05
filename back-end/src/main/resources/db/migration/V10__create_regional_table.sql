CREATE TABLE regional (
    id BIGSERIAL PRIMARY KEY,
    external_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_regional_external_id ON regional(external_id);
CREATE INDEX idx_regional_active ON regional(active);
