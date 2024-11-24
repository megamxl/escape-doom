package com.escapedoom.gamesession.rest.model.escaperoom;

import com.escapedoom.gamesession.dataaccess.entity.Player;
import lombok.Data;

@Data
public class LeaderboardDao {

    private String PlayerName;

    private Long score;

    private Long time;

    public LeaderboardDao(Player player) {
        this.PlayerName = player.getName();
        this.score = player.getScore();
        this.time = player.getLastStageSolved();
    }
}
