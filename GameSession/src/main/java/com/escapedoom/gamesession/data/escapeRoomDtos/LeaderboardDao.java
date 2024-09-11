package com.escapedoom.gamesession.data.escapeRoomDtos;

import com.escapedoom.gamesession.data.Player;
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
