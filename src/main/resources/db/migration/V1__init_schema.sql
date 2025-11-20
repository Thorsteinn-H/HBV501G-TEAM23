-- V1__init_schema.sql
-- Full schema reconstruction with CASCADE drops

-- ========================================
-- Drop all dependent objects first
-- ========================================
DROP TABLE IF EXISTS favorites CASCADE;
DROP TABLE IF EXISTS images CASCADE;
DROP TABLE IF EXISTS matches CASCADE;
DROP TABLE IF EXISTS players CASCADE;
DROP TABLE IF EXISTS teams CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS venues CASCADE;
DROP TABLE IF EXISTS countries CASCADE;

DROP SEQUENCE IF EXISTS favorite_sequence CASCADE;
DROP SEQUENCE IF EXISTS image_sequence CASCADE;
DROP SEQUENCE IF EXISTS match_player_sequence CASCADE;
DROP SEQUENCE IF EXISTS match_sequence CASCADE;
DROP SEQUENCE IF EXISTS player_sequence CASCADE;
DROP SEQUENCE IF EXISTS team_sequence CASCADE;
DROP SEQUENCE IF EXISTS user_sequence CASCADE;
DROP SEQUENCE IF EXISTS venue_sequence CASCADE;

DROP TYPE IF EXISTS favorite_type_enum CASCADE;
DROP TYPE IF EXISTS gender_enum CASCADE;
DROP TYPE IF EXISTS player_position_enum CASCADE;
DROP TYPE IF EXISTS role_enum CASCADE;
DROP TYPE IF EXISTS team_enum CASCADE;

-- ========================================
-- Create enums
-- ========================================
CREATE TYPE favorite_type_enum AS ENUM ('PLAYER','TEAM','MATCH');
CREATE TYPE gender_enum AS ENUM ('MALE','FEMALE','OTHER');
CREATE TYPE player_position_enum AS ENUM ('GOALKEEPER','DEFENDER','MIDFIELDER','FORWARD');
CREATE TYPE role_enum AS ENUM ('USER','ADMIN');
CREATE TYPE team_enum AS ENUM ('SOME_TEAM_ENUM'); -- adjust as needed

-- ========================================
-- Create sequences
-- ========================================
CREATE SEQUENCE favorite_sequence START 1;
CREATE SEQUENCE image_sequence START 1;
CREATE SEQUENCE match_player_sequence START 1;
CREATE SEQUENCE match_sequence START 1;
CREATE SEQUENCE player_sequence START 1;
CREATE SEQUENCE team_sequence START 1;
CREATE SEQUENCE user_sequence START 1;
CREATE SEQUENCE venue_sequence START 1;

-- ========================================
-- Create tables
-- ========================================

-- Countries
CREATE TABLE countries (
    code CHAR(2) PRIMARY KEY,
    country_name VARCHAR(255) NOT NULL
);

-- Venues
CREATE TABLE venues (
    venue_id BIGINT PRIMARY KEY DEFAULT nextval('venue_sequence'),
    venue_name VARCHAR(160) NOT NULL,
    address VARCHAR(255),
    latitude NUMERIC,
    longitude NUMERIC,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

-- Teams
CREATE TABLE teams (
    team_id BIGINT PRIMARY KEY DEFAULT nextval('team_sequence'),
    team_name VARCHAR(120) NOT NULL,
    team_country VARCHAR(80) NOT NULL REFERENCES countries(code),
    venue_id BIGINT REFERENCES venues(venue_id),
    is_active BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

-- Players
CREATE TABLE players (
     player_id BIGINT PRIMARY KEY DEFAULT nextval('player_sequence'),
     player_name VARCHAR(120) NOT NULL,
     player_country CHAR(2) REFERENCES countries(code),
     team_id BIGINT REFERENCES teams(team_id),
     player_position VARCHAR(20) NOT NULL,
     date_of_birth DATE,
     gender VARCHAR(10),
     goals INTEGER NOT NULL DEFAULT 0,
     is_active BOOLEAN NOT NULL,
     created_at TIMESTAMP NOT NULL DEFAULT now(),
     updated_at TIMESTAMP NOT NULL DEFAULT now(),
     CONSTRAINT chk_gender CHECK (gender IN ('MALE', 'FEMALE', 'OTHER')),
     CONSTRAINT chk_players_dob_past CHECK (date_of_birth <= CURRENT_DATE)
);

-- Matches
CREATE TABLE matches (
     match_id BIGINT PRIMARY KEY DEFAULT nextval('match_sequence'),
     home_team_id BIGINT NOT NULL REFERENCES teams(team_id),
     away_team_id BIGINT NOT NULL REFERENCES teams(team_id),
     home_goals INTEGER NOT NULL CHECK (home_goals >= 0),
     away_goals INTEGER NOT NULL CHECK (away_goals >= 0),
     match_date DATE NOT NULL,
     venue_id BIGINT NOT NULL REFERENCES venues(venue_id),
     created_at TIMESTAMP NOT NULL DEFAULT now(),
     updated_at TIMESTAMP NOT NULL DEFAULT now(),
     CONSTRAINT chk_home_away_team CHECK (home_team_id <> away_team_id)
);

-- Images
CREATE TABLE images (
    id BIGINT PRIMARY KEY DEFAULT nextval('image_sequence'),
    image_type VARCHAR(255) NOT NULL,
    image_data BYTEA NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

-- Users
CREATE TABLE users (
    user_id BIGINT PRIMARY KEY DEFAULT nextval('user_sequence'),
    user_name VARCHAR(120) NOT NULL,
    email VARCHAR(320) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    gender VARCHAR(10),
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    deleted_at TIMESTAMP,
    profile_image_id BIGINT REFERENCES images(id),
    image_type VARCHAR(255),
    updated_at TIMESTAMP NOT NULL DEFAULT now(),
    CONSTRAINT chk_gender CHECK (gender IN ('MALE', 'FEMALE', 'OTHER')),
    CONSTRAINT chk_role CHECK (role IN ('USER', 'ADMIN'))
);

-- Favorites
CREATE TABLE favorites (
    favorite_id BIGINT PRIMARY KEY DEFAULT nextval('favorite_sequence'),
    user_id BIGINT NOT NULL,
    entity_type VARCHAR(20) NOT NULL,
    entity_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now(),
    CONSTRAINT uk_user_entity UNIQUE (user_id, entity_type, entity_id)
);

-- Indexes for optimization
CREATE INDEX idx_players_country ON players(player_country);
CREATE INDEX idx_players_team_id ON players(team_id);
CREATE INDEX idx_teams_country ON teams(team_country);
CREATE INDEX idx_teams_team_name ON teams(team_name);
CREATE INDEX idx_teams_venue_id ON teams(venue_id);
CREATE INDEX idx_matches_home_team_id ON matches(home_team_id);
CREATE INDEX idx_matches_away_team_id ON matches(away_team_id);
CREATE INDEX idx_matches_venue_id ON matches(venue_id);
CREATE INDEX idx_favorites_user_id ON favorites(user_id);
CREATE INDEX idx_favorites_entity_id ON favorites(entity_id);
