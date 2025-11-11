--
-- PostgreSQL database dump
--

\restrict STyFIqW25O778oBe32BXc8X97Ava3247zqsPYFa1dneCK8ZSHINof1gwnCqQhki

-- Dumped from database version 17.6 (Debian 17.6-2.pgdg12+1)
-- Dumped by pg_dump version 17.6 (Homebrew)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: public; Type: SCHEMA; Schema: -; Owner: -
--

-- *not* creating schema, since initdb creates it


--
-- Name: player_position_enum; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public.player_position_enum AS ENUM (
    'GOALKEEPER',
    'DEFENDER',
    'MIDFIELDER',
    'FORWARD'
);


--
-- Name: team_enum; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public.team_enum AS ENUM (
    'HOME',
    'AWAY'
);


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: favorite; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.favorite (
    favorite_id bigint NOT NULL,
    entity_id bigint NOT NULL,
    entity_type character varying(255) NOT NULL,
    user_id bigint NOT NULL,
    CONSTRAINT favorite_entity_type_check CHECK (((entity_type)::text = ANY ((ARRAY['PLAYER'::character varying, 'TEAM'::character varying, 'MATCH'::character varying])::text[])))
);


--
-- Name: favorite_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.favorite_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: favorites; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.favorites (
    created_at timestamp(6) without time zone NOT NULL,
    updated_at timestamp(6) without time zone NOT NULL,
    user_id bigint NOT NULL,
    matches text,
    players text,
    scores text,
    teams text,
    venues text
);


--
-- Name: favorites_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.favorites_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: match; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.match (
    away_goals integer NOT NULL,
    home_goals integer NOT NULL,
    match_date date NOT NULL,
    away_team_id bigint NOT NULL,
    home_team_id bigint NOT NULL,
    match_id bigint NOT NULL,
    venue_id bigint NOT NULL
);


--
-- Name: match_player; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.match_player (
    goals_in_match integer,
    match_id bigint,
    match_player_id bigint NOT NULL,
    player_id bigint
);


--
-- Name: match_player_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.match_player_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: match_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.match_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: player; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.player (
    date_of_birth date,
    goals integer NOT NULL,
    is_active boolean NOT NULL,
    player_id bigint NOT NULL,
    team_id bigint,
    player_position character varying(40) NOT NULL,
    player_country character varying(80),
    player_name character varying(120) NOT NULL,
    CONSTRAINT player_player_position_check CHECK (((player_position)::text = ANY ((ARRAY['GOALKEEPER'::character varying, 'DEFENDER'::character varying, 'MIDFIELDER'::character varying, 'FORWARD'::character varying])::text[])))
);


--
-- Name: player_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.player_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: score; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.score (
    score_id bigint NOT NULL,
    away_goals integer NOT NULL,
    home_goals integer NOT NULL
);


--
-- Name: score_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.score_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: spring_session; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.spring_session (
    primary_id character(36) NOT NULL,
    session_id character(36) NOT NULL,
    creation_time bigint NOT NULL,
    last_access_time bigint NOT NULL,
    max_inactive_interval integer NOT NULL,
    expiry_time bigint NOT NULL,
    principal_name character varying(100)
);


--
-- Name: spring_session_attributes; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.spring_session_attributes (
    session_primary_id character(36) NOT NULL,
    attribute_name character varying(200) NOT NULL,
    attribute_bytes bytea NOT NULL
);


--
-- Name: team; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.team (
    is_active boolean NOT NULL,
    team_id bigint NOT NULL,
    venue_id bigint,
    team_country character varying(80) NOT NULL,
    team_name character varying(120) NOT NULL
);


--
-- Name: team_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.team_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: user_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.user_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: users; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.users (
    is_active boolean NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    deleted_at timestamp(6) without time zone,
    user_id bigint NOT NULL,
    user_name character varying(120) NOT NULL,
    email character varying(320) NOT NULL,
    gender character varying(255),
    image_type character varying(255),
    password_hash character varying(255) NOT NULL,
    role character varying(255) NOT NULL,
    image oid
);


--
-- Name: venue; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.venue (
    venue_id bigint NOT NULL,
    latitude character varying(255),
    longitude character varying(255),
    venue_name character varying(160) NOT NULL,
    address character varying(255)
);


--
-- Name: venue_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.venue_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: venues; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.venues (
    venue_id bigint NOT NULL,
    venue_name character varying(160) NOT NULL,
    address character varying(255),
    latitude character varying(255),
    longitude character varying(255)
);


--
-- Name: favorite favorite_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.favorite
    ADD CONSTRAINT favorite_pkey PRIMARY KEY (favorite_id);


--
-- Name: favorites favorites_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.favorites
    ADD CONSTRAINT favorites_pkey PRIMARY KEY (user_id);


--
-- Name: match match_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.match
    ADD CONSTRAINT match_pkey PRIMARY KEY (match_id);


--
-- Name: match_player match_player_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.match_player
    ADD CONSTRAINT match_player_pkey PRIMARY KEY (match_player_id);


--
-- Name: player player_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.player
    ADD CONSTRAINT player_pkey PRIMARY KEY (player_id);


--
-- Name: score score_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.score
    ADD CONSTRAINT score_pkey PRIMARY KEY (score_id);


--
-- Name: spring_session_attributes spring_session_attributes_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.spring_session_attributes
    ADD CONSTRAINT spring_session_attributes_pk PRIMARY KEY (session_primary_id, attribute_name);


--
-- Name: spring_session spring_session_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.spring_session
    ADD CONSTRAINT spring_session_pk PRIMARY KEY (primary_id);


--
-- Name: team team_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.team
    ADD CONSTRAINT team_pkey PRIMARY KEY (team_id);


--
-- Name: favorite uk_user_entity; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.favorite
    ADD CONSTRAINT uk_user_entity UNIQUE (user_id, entity_type, entity_id);


--
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (user_id);


--
-- Name: venue venue_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.venue
    ADD CONSTRAINT venue_pkey PRIMARY KEY (venue_id);


--
-- Name: venues venues_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.venues
    ADD CONSTRAINT venues_pkey PRIMARY KEY (venue_id);


--
-- Name: spring_session_ix1; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX spring_session_ix1 ON public.spring_session USING btree (session_id);


--
-- Name: spring_session_ix2; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX spring_session_ix2 ON public.spring_session USING btree (expiry_time);


--
-- Name: spring_session_ix3; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX spring_session_ix3 ON public.spring_session USING btree (principal_name);


--
-- Name: match fk6ihefb9r7f0fcm0xuves72b2l; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.match
    ADD CONSTRAINT fk6ihefb9r7f0fcm0xuves72b2l FOREIGN KEY (home_team_id) REFERENCES public.team(team_id);


--
-- Name: player fkdvd6ljes11r44igawmpm1mc5s; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.player
    ADD CONSTRAINT fkdvd6ljes11r44igawmpm1mc5s FOREIGN KEY (team_id) REFERENCES public.team(team_id);


--
-- Name: team fkrr9h52i2qcllojvd5d7op5b10; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.team
    ADD CONSTRAINT fkrr9h52i2qcllojvd5d7op5b10 FOREIGN KEY (venue_id) REFERENCES public.venues(venue_id);


--
-- Name: match fksyjor2anx7bkbst7ebyw13jcs; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.match
    ADD CONSTRAINT fksyjor2anx7bkbst7ebyw13jcs FOREIGN KEY (away_team_id) REFERENCES public.team(team_id);


--
-- Name: match fktel163gr0q60uptsprfg64dep; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.match
    ADD CONSTRAINT fktel163gr0q60uptsprfg64dep FOREIGN KEY (venue_id) REFERENCES public.venues(venue_id);


--
-- Name: spring_session_attributes spring_session_attributes_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.spring_session_attributes
    ADD CONSTRAINT spring_session_attributes_fk FOREIGN KEY (session_primary_id) REFERENCES public.spring_session(primary_id) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

\unrestrict STyFIqW25O778oBe32BXc8X97Ava3247zqsPYFa1dneCK8ZSHINof1gwnCqQhki

