DROP TABLE IF EXISTS user_account CASCADE;
DROP TABLE IF EXISTS organization;
DROP TYPE IF EXISTS use_case_t;

CREATE TYPE use_case_t AS ENUM ('office', 'school', 'home');

CREATE TABLE IF NOT EXISTS organization (
    org_id serial PRIMARY KEY,
    org_name text,
    owner_email text NOT NULL
);

CREATE TABLE IF NOT EXISTS user_account (
    email text PRIMARY KEY CHECK ( email ~ '^[a-zA-Z0-9.!#$%&''*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$' ),
    username varchar(30),
    passwd varchar(30),
    use_case use_case_t,
    org_id integer REFERENCES organization(org_id)
);

ALTER TABLE organization ADD CONSTRAINT fk_owner_id FOREIGN KEY (owner_email) REFERENCES user_account(email);

DROP TABLE IF EXISTS temperature_entry;
DROP TABLE IF EXISTS humidity_entry;
DROP TABLE IF EXISTS co2_entry;
DROP TABLE IF EXISTS noise_entry;
DROP TABLE IF EXISTS sound_spike;
DROP TABLE IF EXISTS raw_sound_entry;
DROP TABLE IF EXISTS room;

CREATE TABLE room (
      room_id serial PRIMARY KEY,
      room_name varchar(60) not null,
      account text NOT NULL REFERENCES user_account(email),
      width real,
      "length" real,
      height real
);

CREATE TABLE IF NOT EXISTS temperature_entry (
     room_id integer REFERENCES room(room_id),
     "value" integer,
     "timestamp" timestamptz PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS humidity_entry (
      room_id integer REFERENCES room(room_id),
      "value" integer,
      "timestamp" timestamptz PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS co2_entry (
     room_id integer REFERENCES room(room_id),
     "value" integer,
     "timestamp" timestamptz PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS noise_entry (
       room_id integer REFERENCES room(room_id),
       "value" integer,
       "timestamp" timestamptz PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS raw_sound_entry (
       room_id integer REFERENCES room(room_id),
       "value" integer,
       "timestamp" timestamptz PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS sound_spike (
       room_id integer REFERENCES room(room_id),
       spike_id serial PRIMARY KEY,
       start_entry timestamptz NOT NULL REFERENCES raw_sound_entry(timestamp),
       end_entry timestamptz NOT NULL REFERENCES raw_sound_entry(timestamp)
);
