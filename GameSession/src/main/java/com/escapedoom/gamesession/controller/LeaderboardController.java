package com.escapedoom.gamesession.controller;

import com.escapedoom.gamesession.data.escapeRoomDtos.LeaderboardDao;
import com.escapedoom.gamesession.services.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.escapedoom.gamesession.controller.UrlConstants.API_LEADERBOARD_PATH;
import static com.escapedoom.gamesession.controller.UrlConstants.LEADERBOARD_URL;

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
