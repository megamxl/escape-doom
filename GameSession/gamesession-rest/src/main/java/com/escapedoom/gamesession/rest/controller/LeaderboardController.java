package com.escapedoom.gamesession.rest.controller;

import com.escapedoom.gamesession.rest.model.escaperoom.LeaderboardDao;
import com.escapedoom.gamesession.rest.services.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.escapedoom.gamesession.rest.Constants.API_LEADERBOARD_PATH;
import static com.escapedoom.gamesession.rest.Constants.LEADERBOARD_URL;

@RequiredArgsConstructor
@RestController
@RequestMapping(API_LEADERBOARD_PATH)
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @GetMapping(LEADERBOARD_URL)
    public List<LeaderboardDao> leaderboardAsJson(@PathVariable Long escaperoom_id) {
        return leaderboardService.getScoreBoard(escaperoom_id);
    }
}
