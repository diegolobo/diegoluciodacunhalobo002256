-- Add regional_id column to artist table
ALTER TABLE artist ADD COLUMN regional_id BIGINT;

-- Add foreign key constraint
ALTER TABLE artist ADD CONSTRAINT fk_artist_regional
    FOREIGN KEY (regional_id) REFERENCES regional(id);

-- Create index for better query performance
CREATE INDEX idx_artist_regional_id ON artist(regional_id);
