CREATE TABLE users
(
username character varying(80),
key character varying(200) NOT NULL,
locked boolean DEFAULT false,
id serial NOT NULL,
CONSTRAINT users_username_key UNIQUE (username)
)
WITH (
OIDS=FALSE
);
ALTER TABLE users
OWNER TO postgres;


CREATE INDEX username_idx
ON users
USING btree
(username COLLATE pg_catalog."default");


CREATE UNIQUE INDEX username_idx_lower
ON users
USING btree
(lower(username::text) COLLATE pg_catalog."default");


CREATE TABLE authorities
(
username character varying(80) NOT NULL,
authority character varying(80) NOT NULL,
CONSTRAINT fk_authorities_users FOREIGN KEY (username)
REFERENCES users (username) MATCH SIMPLE
ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
OIDS=FALSE
);
ALTER TABLE authorities
OWNER TO postgres;


CREATE UNIQUE INDEX ix_auth_username
ON authorities
USING btree
(username COLLATE pg_catalog."default", authority COLLATE pg_catalog."default");

INSERT INTO users (username, key, locked) values ('admin', 'ckEp6WxJCG179tWVbQG7gL8gJ5YKzdrCHlgRTX/QvuYWfbr1BpKxlZKlyM3qFMiR', false);

INSERT INTO authorities (username, authority) values ('admin', 'ROLE_USER');
INSERT INTO authorities (username, authority) values ('admin', 'ROLE_ADMIN');

alter table authorities add column createdby varchar(80);
alter table authorities add column createddate timestamp;
alter table authorities add column lastmodifiedby varchar(80);
alter table authorities add column lastmodifieddate timestamp;

alter table users add column version bigint;