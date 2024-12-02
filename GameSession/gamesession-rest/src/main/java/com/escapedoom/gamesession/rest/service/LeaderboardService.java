package com.escapedoom.gamesession.rest.service;

import com.escapedoom.gamesession.dataaccess.entity.Player;
import com.escapedoom.gamesession.rest.model.escaperoom.LeaderboardDao;
import com.escapedoom.gamesession.dataaccess.SessionManagementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LeaderboardService {

    private final SessionManagementRepository repository;

    public List<LeaderboardDao> getScoreBoard(Long escaperoomID) {
        List<LeaderboardDao> playerScores = new ArrayList<>();
        Optional<List<Player>> allPlayerOfEscaperoom = repository.findAllByEscaperoomSession(escaperoomID);
        if (allPlayerOfEscaperoom.isPresent()) {
            for (Player p : allPlayerOfEscaperoom.get()) {
                playerScores.add(new LeaderboardDao(p));
            }
        }
        return playerScores;
    }


}
