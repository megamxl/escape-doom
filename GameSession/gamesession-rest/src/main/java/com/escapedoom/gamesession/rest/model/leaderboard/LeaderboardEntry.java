package com.escapedoom.gamesession.rest.model.leaderboard;

import com.escapedoom.gamesession.dataaccess.entity.Player;
import lombok.Data;

@Data
public class LeaderboardEntry {

    private String PlayerName;

    private Long score;

    private Long time;

    public LeaderboardEntry(Player player) {
        this.PlayerName = player.getName();
        this.score = player.getScore();
        this.time = player.getLastStageSolved();
    }
}
