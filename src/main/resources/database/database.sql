CREATE SEQUENCE public."usuarios_ID_seq"
    INCREMENT 1
    START 7
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

ALTER SEQUENCE public."usuarios_ID_seq"
    OWNER TO postgres;

    -- Table: public.usuarios

-- DROP TABLE public.usuarios;

CREATE TABLE public.usuarios
(
    "ID" integer NOT NULL DEFAULT nextval('"usuarios_ID_seq"'::regclass),
    usuario text COLLATE pg_catalog."default" NOT NULL,
    password text COLLATE pg_catalog."default" NOT NULL,
    marquita text COLLATE pg_catalog."default",
    "Email" text COLLATE pg_catalog."default",
    CONSTRAINT usuarios_pkey PRIMARY KEY ("ID")
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.usuarios
    OWNER to postgres;