--
-- PostgreSQL database dump
--

\restrict RThwdrWFin3mlLFCRrQoq7CjcZxNhk1SWgKR514rckbhwQsz7u0QvqyuPtHeibv

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
-- Name: public; Type: SCHEMA; Schema: -; Owner: admin
--

-- *not* creating schema, since initdb creates it


ALTER SCHEMA public OWNER TO admin;

--
-- Name: pg_stat_statements; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS pg_stat_statements WITH SCHEMA public;


--
-- Name: EXTENSION pg_stat_statements; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION pg_stat_statements IS 'track planning and execution statistics of all SQL statements executed';


--
-- Name: player_position_enum; Type: TYPE; Schema: public; Owner: admin
--

CREATE TYPE public.player_position_enum AS ENUM (
    'GOALKEEPER',
    'DEFENDER',
    'MIDFIELDER',
    'FORWARD'
);


ALTER TYPE public.player_position_enum OWNER TO admin;

--
-- Name: team_enum; Type: TYPE; Schema: public; Owner: admin
--

CREATE TYPE public.team_enum AS ENUM (
    'HOME',
    'AWAY'
);


ALTER TYPE public.team_enum OWNER TO admin;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: favorite; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.favorite (
    favorite_id bigint NOT NULL,
    entity_id bigint NOT NULL,
    entity_type character varying(255) NOT NULL,
    user_id bigint NOT NULL,
    CONSTRAINT favorite_entity_type_check CHECK (((entity_type)::text = ANY ((ARRAY['PLAYER'::character varying, 'TEAM'::character varying, 'MATCH'::character varying])::text[])))
);


ALTER TABLE public.favorite OWNER TO admin;

--
-- Name: favorite_sequence; Type: SEQUENCE; Schema: public; Owner: admin
--

CREATE SEQUENCE public.favorite_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.favorite_sequence OWNER TO admin;

--
-- Name: favorites_sequence; Type: SEQUENCE; Schema: public; Owner: admin
--

CREATE SEQUENCE public.favorites_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.favorites_sequence OWNER TO admin;

--
-- Name: score; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.score (
    score_id bigint NOT NULL,
    away_goals integer NOT NULL,
    home_goals integer NOT NULL
);


ALTER TABLE public.score OWNER TO admin;

--
-- Name: score_sequence; Type: SEQUENCE; Schema: public; Owner: admin
--

CREATE SEQUENCE public.score_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.score_sequence OWNER TO admin;

--
-- Name: spring_session; Type: TABLE; Schema: public; Owner: admin
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


ALTER TABLE public.spring_session OWNER TO admin;

--
-- Name: spring_session_attributes; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.spring_session_attributes (
    session_primary_id character(36) NOT NULL,
    attribute_name character varying(200) NOT NULL,
    attribute_bytes bytea NOT NULL
);


ALTER TABLE public.spring_session_attributes OWNER TO admin;

--
-- Name: venue; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.venue (
    venue_id bigint NOT NULL,
    latitude character varying(255),
    longitude character varying(255),
    venue_name character varying(160) NOT NULL,
    address character varying(255)
);


ALTER TABLE public.venue OWNER TO admin;

--
-- Data for Name: favorite; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.favorite (favorite_id, entity_id, entity_type, user_id) FROM stdin;
4	6	MATCH	41
\.


--
-- Data for Name: score; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.score (score_id, away_goals, home_goals) FROM stdin;
\.


--
-- Data for Name: spring_session; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.spring_session (primary_id, session_id, creation_time, last_access_time, max_inactive_interval, expiry_time, principal_name) FROM stdin;
\.


--
-- Data for Name: spring_session_attributes; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.spring_session_attributes (session_primary_id, attribute_name, attribute_bytes) FROM stdin;
\.


--
-- Data for Name: venue; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.venue (venue_id, latitude, longitude, venue_name, address) FROM stdin;
1	63.844444	-22.429278	Stakkavíkurvöllur	\N
2	\N	\N	Laugardalsvöllur	Reykjavegur 15, 104 Reykjavík, Ísland
3	\N	\N	Þórsvöllur	Boginn, Hamar, Skarðshlíð, 603 Akureyri
4	\N	\N	Kópavogsvöllur	Dalsmári 5, 201 Kópavogur, Ísland
5	\N	\N	Kórinn	Kórinn, Vallakór 12-14, 203 Kópavogur
\.


--
-- Name: favorite_sequence; Type: SEQUENCE SET; Schema: public; Owner: admin
--

SELECT pg_catalog.setval('public.favorite_sequence', 5, true);


--
-- Name: favorites_sequence; Type: SEQUENCE SET; Schema: public; Owner: admin
--

SELECT pg_catalog.setval('public.favorites_sequence', 1, false);


--
-- Name: score_sequence; Type: SEQUENCE SET; Schema: public; Owner: admin
--

SELECT pg_catalog.setval('public.score_sequence', 1, false);


--
-- Name: favorite favorite_pkey; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.favorite
    ADD CONSTRAINT favorite_pkey PRIMARY KEY (favorite_id);


--
-- Name: score score_pkey; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.score
    ADD CONSTRAINT score_pkey PRIMARY KEY (score_id);


--
-- Name: spring_session_attributes spring_session_attributes_pk; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.spring_session_attributes
    ADD CONSTRAINT spring_session_attributes_pk PRIMARY KEY (session_primary_id, attribute_name);


--
-- Name: spring_session spring_session_pk; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.spring_session
    ADD CONSTRAINT spring_session_pk PRIMARY KEY (primary_id);


--
-- Name: favorite uk_user_entity; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.favorite
    ADD CONSTRAINT uk_user_entity UNIQUE (user_id, entity_type, entity_id);


--
-- Name: venue venue_pkey; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.venue
    ADD CONSTRAINT venue_pkey PRIMARY KEY (venue_id);


--
-- Name: spring_session_ix1; Type: INDEX; Schema: public; Owner: admin
--

CREATE UNIQUE INDEX spring_session_ix1 ON public.spring_session USING btree (session_id);


--
-- Name: spring_session_ix2; Type: INDEX; Schema: public; Owner: admin
--

CREATE INDEX spring_session_ix2 ON public.spring_session USING btree (expiry_time);


--
-- Name: spring_session_ix3; Type: INDEX; Schema: public; Owner: admin
--

CREATE INDEX spring_session_ix3 ON public.spring_session USING btree (principal_name);


--
-- Name: spring_session_attributes spring_session_attributes_fk; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.spring_session_attributes
    ADD CONSTRAINT spring_session_attributes_fk FOREIGN KEY (session_primary_id) REFERENCES public.spring_session(primary_id) ON DELETE CASCADE;


--
-- Name: FUNCTION pg_stat_statements(showtext boolean, OUT userid oid, OUT dbid oid, OUT toplevel boolean, OUT queryid bigint, OUT query text, OUT plans bigint, OUT total_plan_time double precision, OUT min_plan_time double precision, OUT max_plan_time double precision, OUT mean_plan_time double precision, OUT stddev_plan_time double precision, OUT calls bigint, OUT total_exec_time double precision, OUT min_exec_time double precision, OUT max_exec_time double precision, OUT mean_exec_time double precision, OUT stddev_exec_time double precision, OUT rows bigint, OUT shared_blks_hit bigint, OUT shared_blks_read bigint, OUT shared_blks_dirtied bigint, OUT shared_blks_written bigint, OUT local_blks_hit bigint, OUT local_blks_read bigint, OUT local_blks_dirtied bigint, OUT local_blks_written bigint, OUT temp_blks_read bigint, OUT temp_blks_written bigint, OUT shared_blk_read_time double precision, OUT shared_blk_write_time double precision, OUT local_blk_read_time double precision, OUT local_blk_write_time double precision, OUT temp_blk_read_time double precision, OUT temp_blk_write_time double precision, OUT wal_records bigint, OUT wal_fpi bigint, OUT wal_bytes numeric, OUT jit_functions bigint, OUT jit_generation_time double precision, OUT jit_inlining_count bigint, OUT jit_inlining_time double precision, OUT jit_optimization_count bigint, OUT jit_optimization_time double precision, OUT jit_emission_count bigint, OUT jit_emission_time double precision, OUT jit_deform_count bigint, OUT jit_deform_time double precision, OUT stats_since timestamp with time zone, OUT minmax_stats_since timestamp with time zone); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.pg_stat_statements(showtext boolean, OUT userid oid, OUT dbid oid, OUT toplevel boolean, OUT queryid bigint, OUT query text, OUT plans bigint, OUT total_plan_time double precision, OUT min_plan_time double precision, OUT max_plan_time double precision, OUT mean_plan_time double precision, OUT stddev_plan_time double precision, OUT calls bigint, OUT total_exec_time double precision, OUT min_exec_time double precision, OUT max_exec_time double precision, OUT mean_exec_time double precision, OUT stddev_exec_time double precision, OUT rows bigint, OUT shared_blks_hit bigint, OUT shared_blks_read bigint, OUT shared_blks_dirtied bigint, OUT shared_blks_written bigint, OUT local_blks_hit bigint, OUT local_blks_read bigint, OUT local_blks_dirtied bigint, OUT local_blks_written bigint, OUT temp_blks_read bigint, OUT temp_blks_written bigint, OUT shared_blk_read_time double precision, OUT shared_blk_write_time double precision, OUT local_blk_read_time double precision, OUT local_blk_write_time double precision, OUT temp_blk_read_time double precision, OUT temp_blk_write_time double precision, OUT wal_records bigint, OUT wal_fpi bigint, OUT wal_bytes numeric, OUT jit_functions bigint, OUT jit_generation_time double precision, OUT jit_inlining_count bigint, OUT jit_inlining_time double precision, OUT jit_optimization_count bigint, OUT jit_optimization_time double precision, OUT jit_emission_count bigint, OUT jit_emission_time double precision, OUT jit_deform_count bigint, OUT jit_deform_time double precision, OUT stats_since timestamp with time zone, OUT minmax_stats_since timestamp with time zone) TO admin;


--
-- Name: FUNCTION pg_stat_statements_info(OUT dealloc bigint, OUT stats_reset timestamp with time zone); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION public.pg_stat_statements_info(OUT dealloc bigint, OUT stats_reset timestamp with time zone) TO admin;


--
-- Name: DEFAULT PRIVILEGES FOR SEQUENCES; Type: DEFAULT ACL; Schema: -; Owner: postgres
--

ALTER DEFAULT PRIVILEGES FOR ROLE postgres GRANT ALL ON SEQUENCES TO admin;


--
-- Name: DEFAULT PRIVILEGES FOR TYPES; Type: DEFAULT ACL; Schema: -; Owner: postgres
--

ALTER DEFAULT PRIVILEGES FOR ROLE postgres GRANT ALL ON TYPES TO admin;


--
-- Name: DEFAULT PRIVILEGES FOR FUNCTIONS; Type: DEFAULT ACL; Schema: -; Owner: postgres
--

ALTER DEFAULT PRIVILEGES FOR ROLE postgres GRANT ALL ON FUNCTIONS TO admin;


--
-- Name: DEFAULT PRIVILEGES FOR TABLES; Type: DEFAULT ACL; Schema: -; Owner: postgres
--

ALTER DEFAULT PRIVILEGES FOR ROLE postgres GRANT ALL ON TABLES TO admin;


--
-- PostgreSQL database dump complete
--

\unrestrict RThwdrWFin3mlLFCRrQoq7CjcZxNhk1SWgKR514rckbhwQsz7u0QvqyuPtHeibv

