package lector.portal.dataaccess.query;

public class LobbyNativeQueries {

    public static final String FIND_ALL_BY_STATE_PLAYING = "FROM OpenLobbys os WHERE os.state = 'PLAYING'";

    public static final String FIND_BY_ESCAPEROOM_AND_USER_AND_STATE_STOPPED_NOT = 
        "SELECT * FROM open_lobbys op " +
        "WHERE op.state NOT LIKE 'STOPPED' " +
        "AND op.escaperoom_escaperoom_id = ?1";
}
