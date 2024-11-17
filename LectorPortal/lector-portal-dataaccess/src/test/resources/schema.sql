-- Drop existing tables
DROP TABLE IF EXISTS escape_room_stage;
DROP TABLE IF EXISTS open_lobbies;
DROP TABLE IF EXISTS escaperoom;
DROP TABLE IF EXISTS user_info;

-- Drop sequences
DROP SEQUENCE IF EXISTS escape_room_stage_seq;
DROP SEQUENCE IF EXISTS escaperoom_seq;
DROP SEQUENCE IF EXISTS user_info_seq;

-- Create sequences
CREATE SEQUENCE escape_room_stage_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE escaperoom_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE user_info_seq START WITH 1 INCREMENT BY 50;

-- Create user_info table
CREATE TABLE user_info (
                           user_id BIGINT NOT NULL DEFAULT NEXT VALUE FOR user_info_seq,
                           lastname VARCHAR(255),
                           firstname VARCHAR(255),
                           email VARCHAR(255),
                           _password VARCHAR(255),
                           role VARCHAR(255),
                           CONSTRAINT pk_user_info PRIMARY KEY (user_id),
                           CONSTRAINT uc_user_info_email UNIQUE (email)
);

-- Create escaperoom table
CREATE TABLE escaperoom (
                            escaperoom_id BIGINT NOT NULL DEFAULT NEXT VALUE FOR escaperoom_seq,
                            user_id BIGINT,
                            name VARCHAR(255),
                            topic VARCHAR(255),
                            time INTEGER,
                            max_stage BIGINT,
                            CONSTRAINT pk_escaperoom PRIMARY KEY (escaperoom_id),
                            CONSTRAINT FK_ESCAPEROOM_ON_USER FOREIGN KEY (user_id) REFERENCES user_info (user_id)
);

-- Create escape_room_stage table
CREATE TABLE escape_room_stage (
                                   id BIGINT NOT NULL DEFAULT NEXT VALUE FOR escape_room_stage_seq,
                                   escape_roomid BIGINT,
                                   stage_id BIGINT,
                                   stage CLOB,
                                   outputid BIGINT,
                                   CONSTRAINT pk_escape_room_stage PRIMARY KEY (id),
                                   CONSTRAINT FK_ESCAPE_ROOM_STAGE_ESCAPEROOM FOREIGN KEY (escape_roomid) REFERENCES escaperoom (escaperoom_id)
);

-- Insert into user_info table
INSERT INTO user_info (firstname, lastname, email, _password, role)
VALUES
    ('John', 'Doe', 'john.doe@example.com', 'password123', 'USER'),
    ('fre', 'leon', 'leon@doom.at', '$2a$10$vzhj0mev75olkb3kBsETre8hoA.Zxef2Gl2aysI8G7M0FroN/a6/.', 'LECTOR' );

-- Insert into escaperoom table
INSERT INTO escaperoom (user_id, name, topic, time, max_stage)
VALUES
    ((SELECT user_id FROM user_info WHERE email = 'john.doe@example.com'), 'Test Escape Room', 'Science', 60, 2),
    ((SELECT user_id FROM user_info WHERE email = 'leon@doom.at'), 'Math Escape Room', 'Math', 45, 1);

-- Insert into escape_room_stage table
UPDATE escape_room_stage
SET stage = '[{"id": 1, "description": "Stage 1"}]'
WHERE stage = 'Stage 1';

UPDATE escape_room_stage
SET stage = '[{"id": 2, "description": "Stage 2"}]'
WHERE stage = 'Stage 2';


