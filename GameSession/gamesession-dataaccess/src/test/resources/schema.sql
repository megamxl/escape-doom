-- Drop existing tables if they exist
DROP TABLE IF EXISTS escape_room_stage;
DROP TABLE IF EXISTS open_lobbies;
DROP TABLE IF EXISTS escaperoom;
DROP TABLE IF EXISTS user_info;

-- Drop sequences if they exist
DROP SEQUENCE IF EXISTS escape_room_stage_seq;
DROP SEQUENCE IF EXISTS escaperoom_seq;
DROP SEQUENCE IF EXISTS user_info_seq;

-- Create sequences for auto-increment
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

-- Table creation
CREATE TABLE IF NOT EXISTS player (
                                      player_Id INT PRIMARY KEY,
                                      name VARCHAR(255),
    httpSessionID VARCHAR(255),
    escaperoomSession INT,
    escaperoomStageId INT,
    escampeRoom_room_id INT,
    score INT,
    lastStageSolved INT
    );

-- Insert data
INSERT INTO player (player_Id, name, httpSessionID, escaperoomSession, escaperoomStageId, escampeRoom_room_id, score, lastStageSolved)
VALUES (1, 'Player1', 'sessionId1', 1, 1, 1, 0, 1),
       (2, 'Player2', 'sessionId2', 1, 2, 1, 0, 2),
       (3, 'Player3', 'sessionId3', 2, 1, 2, 0, 1);


-- Insert test data into user_info table
INSERT INTO user_info (firstname, lastname, email, _password, role)
VALUES
    ('John', 'Doe', 'john.doe@example.com', 'password123', 'USER'),
    ('fre', 'leon', 'leon@doom.at', '$2a$10$vzhj0mev75olkb3kBsETre8hoA.Zxef2Gl2aysI8G7M0FroN/a6/.', 'LECTOR');

-- Insert test data into escaperoom table
INSERT INTO escaperoom (user_id, name, topic, time, max_stage)
VALUES
    ((SELECT user_id FROM user_info WHERE email = 'john.doe@example.com'), 'Test Escape Room', 'Science', 60, 3),
    ((SELECT user_id FROM user_info WHERE email = 'leon@doom.at'), 'Math Escape Room', 'Math', 45, 1);

-- Insert test data into escape_room_stage table
INSERT INTO escape_room_stage (escape_roomid, stage_id, stage, outputid)
VALUES
    (1, 1, '[{"id": 1, "description": "Stage 1"}]', NULL),
    (1, 2, '[{"id": 2, "description": "Stage 2"}]', NULL);



-- Ensure escape_room_stage is properly updated (although this is redundant after above insert)
UPDATE escape_room_stage
SET stage = '[{"id": 1, "description": "Stage 1"}]'
WHERE stage = 'Stage 1';

UPDATE escape_room_stage
SET stage = '[{"id": 2, "description": "Stage 2"}]'
WHERE stage = 'Stage 2';
