package com.escapedoom.gamesession.controller;

import com.escapedoom.gamesession.data.escapeRoomDtos.LeaderboardDao;
import com.escapedoom.gamesession.services.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @GetMapping("/{escaperoom_id}")

    public List<LeaderboardDao> LeaderboardAsJson(@PathVariable Long escaperoom_id) {
        return leaderboardService.getScoreBoard(escaperoom_id);
    }


}
