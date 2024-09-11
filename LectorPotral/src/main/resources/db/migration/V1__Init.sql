CREATE SEQUENCE IF NOT EXISTS console_node_code_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS open_lobbys_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS player_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE console_node_code
(
    id                 BIGINT NOT NULL,
    language           VARCHAR(255),
    function_signature VARCHAR(1000),
    input              VARCHAR(1000),
    variable_name      VARCHAR(1000),
    expected_output    VARCHAR(1000),
    CONSTRAINT pk_consolenodecode PRIMARY KEY (id)
);

CREATE TABLE escape_room_stage
(
    id            BIGINT NOT NULL,
    escape_roomid BIGINT,
    stage_id      BIGINT,
    stage         json,
    outputid      BIGINT,
    CONSTRAINT pk_escape_room_stage PRIMARY KEY (id)
);

CREATE TABLE open_lobbys
(
    lobby_id                 BIGINT NOT NULL,
    escaperoom_escaperoom_id BIGINT,
    user_user_id             BIGINT,
    state                    VARCHAR(255),
    end_time                 TIMESTAMP WITHOUT TIME ZONE,
    start_time               TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_openlobbys PRIMARY KEY (lobby_id)
);

CREATE TABLE player
(
    player_id            INTEGER NOT NULL,
    name                 VARCHAR(255),
    http_sessionid       VARCHAR(255),
    escaperoom_session   BIGINT,
    escaperoom_stage_id  BIGINT,
    escampe_room_room_id BIGINT,
    score                BIGINT,
    last_stage_solved    BIGINT,
    CONSTRAINT pk_player PRIMARY KEY (player_id)
);

CREATE TABLE processing_request
(
    userid           VARCHAR(255) NOT NULL,
    compiling_status VARCHAR(255),
    output           VARCHAR(1000),
    CONSTRAINT pk_processingrequest PRIMARY KEY (userid)
);

ALTER TABLE player
    ADD CONSTRAINT uc_player_http_sessionid UNIQUE (http_sessionid);

CREATE SEQUENCE IF NOT EXISTS escape_room_stage_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS escaperoom_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS open_lobbys_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS player_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS user_info_seq START WITH 1 INCREMENT BY 50;


CREATE TABLE escaperoom
(
    escaperoom_id BIGINT NOT NULL,
    user_id       BIGINT,
    name          VARCHAR(255),
    topic         VARCHAR(255),
    time          INTEGER,
    max_stage     BIGINT,
    CONSTRAINT pk_escaperoom PRIMARY KEY (escaperoom_id)
);

CREATE TABLE user_info
(
    user_id   BIGINT NOT NULL,
    lastname  VARCHAR(255),
    firstname VARCHAR(255),
    email     VARCHAR(255),
    _password VARCHAR(255),
    role      VARCHAR(255),
    CONSTRAINT pk_user_info PRIMARY KEY (user_id)
);

ALTER TABLE user_info
    ADD CONSTRAINT uc_user_info_email UNIQUE (email);

ALTER TABLE escaperoom
    ADD CONSTRAINT FK_ESCAPEROOM_ON_USER FOREIGN KEY (user_id) REFERENCES user_info (user_id);