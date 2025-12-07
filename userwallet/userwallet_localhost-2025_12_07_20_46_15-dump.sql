--
-- PostgreSQL database dump
--

\restrict 9cD2Y6qRB6oysBDwlbcWWpPye1Jqknw5nXKzOESXRfFfMnmdN2tv081CQKVbils

-- Dumped from database version 14.19 (Ubuntu 14.19-0ubuntu0.22.04.1)
-- Dumped by pg_dump version 18.0 (Ubuntu 18.0-1.pgdg22.04+3)

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
-- Name: public; Type: SCHEMA; Schema: -; Owner: postgres
--

-- *not* creating schema, since initdb creates it


ALTER SCHEMA public OWNER TO postgres;

--
-- Name: transaction_status; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.transaction_status AS ENUM (
    'success',
    'failed'
);


ALTER TYPE public.transaction_status OWNER TO postgres;

--
-- Name: insert_transaction(bigint, bigint, numeric, public.transaction_status, text); Type: PROCEDURE; Schema: public; Owner: postgres
--

CREATE PROCEDURE public.insert_transaction(IN sender_id bigint, IN receiver_id bigint, IN amount numeric, IN status public.transaction_status, IN details text)
    LANGUAGE plpgsql
    AS $$
    begin
        insert into transactions(sender, receiver, amount, status, details, created_at)
            values (sender_id, receiver_id, insert_transaction.amount, insert_transaction.status, insert_transaction.details, now());
    end;
    $$;


ALTER PROCEDURE public.insert_transaction(IN sender_id bigint, IN receiver_id bigint, IN amount numeric, IN status public.transaction_status, IN details text) OWNER TO postgres;

--
-- Name: transfermoney(bigint, bigint, numeric); Type: PROCEDURE; Schema: public; Owner: postgres
--

CREATE PROCEDURE public.transfermoney(IN sender_id bigint, IN receiver_id bigint, IN amount numeric)
    LANGUAGE plpgsql
    AS $$
    declare
        sender_balance numeric;
        receiver_exist boolean;
    begin

        -- prevent deadlock if two user try to transfer to each other in the same time
        if sender_id < receiver_id then
            perform 1 from users where id = sender_id for update;
            perform 1 from users where id = receiver_id for update;
        else
            perform 1 from users where id = receiver_id for update;
            perform 1 from users where id = sender_id for update;
        end if;

        -- sender balance
        select wallet_balance into sender_balance from users where id = sender_id;

        -- check if receiver is existing
        select true into receiver_exist from users where id = receiver_id;
        if not found then
            raise exception 'receiver not found';
        end if;

        -- check from sender balance
        if sender_balance < amount then
            call insert_transaction(sender_id, receiver_id, amount, 'failed',
                                'transaction is failed because you dont have enough money');
            commit;
            raise exception 'insufficient balance';
        end if;

        update users set wallet_balance = wallet_balance - amount where id = sender_id;
        update users set wallet_balance = wallet_balance + amount where id = receiver_id;

        call insert_transaction(sender_id, receiver_id, amount, 'success',
                                'transaction is successes');
    end;
    $$;


ALTER PROCEDURE public.transfermoney(IN sender_id bigint, IN receiver_id bigint, IN amount numeric) OWNER TO postgres;

--
-- Name: user_transactions(bigint); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.user_transactions(sender_id bigint) RETURNS TABLE(receiver_name character varying, receiver_phone character varying, amount numeric, transaction_date timestamp without time zone)
    LANGUAGE plpgsql
    AS $$
    begin
        return query
        select u.name as receiver_name, u.phone as receiver_phone,
        transactions.amount, transactions.created_at as transaction_date from transactions
        inner join users u on transactions.receiver = u.id where transactions.sender = sender_id;
    end;
    $$;


ALTER FUNCTION public.user_transactions(sender_id bigint) OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: jwt_sessions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.jwt_sessions (
    token text NOT NULL,
    user_id integer NOT NULL
);


ALTER TABLE public.jwt_sessions OWNER TO postgres;

--
-- Name: jwt_sessions_user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.jwt_sessions_user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.jwt_sessions_user_id_seq OWNER TO postgres;

--
-- Name: jwt_sessions_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.jwt_sessions_user_id_seq OWNED BY public.jwt_sessions.user_id;


--
-- Name: transactions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.transactions (
    id integer NOT NULL,
    sender integer NOT NULL,
    receiver integer NOT NULL,
    amount numeric NOT NULL,
    status public.transaction_status,
    details text,
    created_at timestamp without time zone DEFAULT now(),
    CONSTRAINT transactions_check CHECK ((sender <> receiver))
);


ALTER TABLE public.transactions OWNER TO postgres;

--
-- Name: transactions_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.transactions_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.transactions_id_seq OWNER TO postgres;

--
-- Name: transactions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.transactions_id_seq OWNED BY public.transactions.id;


--
-- Name: transactions_receiver_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.transactions_receiver_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.transactions_receiver_seq OWNER TO postgres;

--
-- Name: transactions_receiver_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.transactions_receiver_seq OWNED BY public.transactions.receiver;


--
-- Name: transactions_sender_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.transactions_sender_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.transactions_sender_seq OWNER TO postgres;

--
-- Name: transactions_sender_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.transactions_sender_seq OWNED BY public.transactions.sender;


--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id integer NOT NULL,
    name character varying(40),
    email character varying(100) NOT NULL,
    password text NOT NULL,
    phone character varying(15) NOT NULL,
    address text,
    image text,
    wallet_balance numeric DEFAULT 0.00,
    created_at timestamp without time zone DEFAULT now(),
    updated_at timestamp without time zone DEFAULT now(),
    status boolean DEFAULT true
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_id_seq OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- Name: jwt_sessions user_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.jwt_sessions ALTER COLUMN user_id SET DEFAULT nextval('public.jwt_sessions_user_id_seq'::regclass);


--
-- Name: transactions id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transactions ALTER COLUMN id SET DEFAULT nextval('public.transactions_id_seq'::regclass);


--
-- Name: transactions sender; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transactions ALTER COLUMN sender SET DEFAULT nextval('public.transactions_sender_seq'::regclass);


--
-- Name: transactions receiver; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transactions ALTER COLUMN receiver SET DEFAULT nextval('public.transactions_receiver_seq'::regclass);


--
-- Name: users id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- Data for Name: jwt_sessions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.jwt_sessions (token, user_id) FROM stdin;
eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJrYXJlZW1AeWFob28uY29tIiwiaWQiOjEsImlhdCI6MTc2NTExNTg5OSwiZXhwIjoxNzY1OTc5ODk5fQ.5eIc4BraHctiRWr1QfklmTbffuMmogbD8bxEYus-HaI-OZ_I5yuV5Ziq8drCsc39	1
\.


--
-- Data for Name: transactions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.transactions (id, sender, receiver, amount, status, details, created_at) FROM stdin;
1	2	1	10	success	transaction is successes	2025-12-03 13:17:21.628898
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, name, email, password, phone, address, image, wallet_balance, created_at, updated_at, status) FROM stdin;
2	Glenna Gross	test@yahoo.com	$2a$10$gIkI3NY10qUg/CF/0JIYaO/SLOwRloBj81Zs/lKLIft2oiS/zLheW	01016899033	Eveniet temporibus 	\N	90	2025-12-03 13:17:13.977127	2025-12-03 13:17:13.977127	t
1	kareem	kareem@yahoo.com	$2a$10$CoY.BvCYT6hZKqVpXOf0P.xTRKXEJGdHeiCTiFep7tvS3x3TOyosm	01016899037	In perspiciatis vol	\N	110	2025-12-03 13:16:37.788691	2025-12-03 13:16:37.788691	t
\.


--
-- Name: jwt_sessions_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.jwt_sessions_user_id_seq', 1, false);


--
-- Name: transactions_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.transactions_id_seq', 1, true);


--
-- Name: transactions_receiver_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.transactions_receiver_seq', 1, false);


--
-- Name: transactions_sender_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.transactions_sender_seq', 1, false);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 2, true);


--
-- Name: jwt_sessions jwt_sessions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.jwt_sessions
    ADD CONSTRAINT jwt_sessions_pkey PRIMARY KEY (token);


--
-- Name: transactions transactions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transactions
    ADD CONSTRAINT transactions_pkey PRIMARY KEY (id);


--
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- Name: users users_phone_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_phone_key UNIQUE (phone);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: jwt_sessions jwt_sessions_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.jwt_sessions
    ADD CONSTRAINT jwt_sessions_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: transactions transactions_receiver_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transactions
    ADD CONSTRAINT transactions_receiver_fkey FOREIGN KEY (receiver) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: transactions transactions_sender_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transactions
    ADD CONSTRAINT transactions_sender_fkey FOREIGN KEY (sender) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE USAGE ON SCHEMA public FROM PUBLIC;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

\unrestrict 9cD2Y6qRB6oysBDwlbcWWpPye1Jqknw5nXKzOESXRfFfMnmdN2tv081CQKVbils

