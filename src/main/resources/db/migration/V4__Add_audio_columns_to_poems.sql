ALTER TABLE poems
    ADD COLUMN audio_data BYTEA,
    ADD COLUMN audio_content_type VARCHAR(100),
    ADD COLUMN audio_filename VARCHAR(255),
    ADD COLUMN audio_compression VARCHAR(20),
    ADD COLUMN audio_original_size INTEGER,
    ADD COLUMN audio_compressed_size INTEGER;
