package com.escapedoom.gamesession.rest;

public class Constants {

    /**
     * URL Constants
     */

    private static final String SEPARATOR = "/";
    private static final String API_PATH_VARIABLE = "api";
    private static final String LOBBY_PATH_VARIABLE = "lobby";
    private static final String LEADERBOARD_PATH_VARIABLE = "leaderboard";

    private static final String ESCAPEROOM_ID_PATH_VARIABLE = "{escaperoom_id}";
    private static final String ID_PATH_VARIABLE = "{id}";
    private static final String PLAYER_ID_PATH_VARIABLE = "{playerID}";
    private static final String HTTP_SESSION_PATH_VARIABLE = "{httpSession}";

    public static final String ERROR_URL = SEPARATOR + "error";

    // IPC
    public static final String IPC_INFO_URL = SEPARATOR + "info";
    public static final String START_GAME_URL = SEPARATOR + "started" + SEPARATOR + ESCAPEROOM_ID_PATH_VARIABLE;

    // Join
    public static final String API_JOIN_PATH = SEPARATOR + API_PATH_VARIABLE + SEPARATOR + "join";
    public static final String ESCAPE_ROOM_URL = SEPARATOR + ESCAPEROOM_ID_PATH_VARIABLE;
    public static final String LOBBY_URL = SEPARATOR + LOBBY_PATH_VARIABLE + SEPARATOR + ID_PATH_VARIABLE;
    public static final String GET_STAGE_URL = SEPARATOR + "getStage" + SEPARATOR + HTTP_SESSION_PATH_VARIABLE;
    public static final String DELETE_URL = SEPARATOR + "delete" + SEPARATOR + ESCAPEROOM_ID_PATH_VARIABLE;
    public static final String GET_ALL_URL = SEPARATOR + "getAll" + SEPARATOR + ESCAPEROOM_ID_PATH_VARIABLE;
    public static final String SUBMIT_CODE_URL = SEPARATOR + "submitCode";
    public static final String GET_CODE_URL = SEPARATOR + "getCode" + SEPARATOR + PLAYER_ID_PATH_VARIABLE;
    public static final String STATUS_URL = SEPARATOR + "status" + SEPARATOR + PLAYER_ID_PATH_VARIABLE;

    // Leaderboard
    public static final String API_LEADERBOARD_PATH = SEPARATOR + API_PATH_VARIABLE + SEPARATOR + LEADERBOARD_PATH_VARIABLE;
    public static final String LEADERBOARD_URL = SEPARATOR + ESCAPEROOM_ID_PATH_VARIABLE;
}
