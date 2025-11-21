ALTER TABLE matches
    ALTER COLUMN match_date TYPE timestamptz
        USING match_date AT TIME ZONE 'UTC';
