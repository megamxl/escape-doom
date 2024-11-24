-- Insert a test escaperoom
INSERT INTO escaperoom (escaperoom_id, name) VALUES (1, 'Test Escape Room');

-- Insert a test user
INSERT INTO user (user_id, firstname, lastname, email) VALUES (1, 'Leon', 'Doom', 'leon@doom.at');

-- Insert a lobby with state PLAYING
INSERT INTO open_lobbys (lobby_id, escaperoom_escaperoom_id, user_user_id, state) VALUES (1, 1, 1, 'PLAYING');

-- Insert a lobby with state STOPPED
INSERT INTO open_lobbys (lobby_id, escaperoom_escaperoom_id, user_user_id, state) VALUES (2, 1, 1, 'STOPPED');
