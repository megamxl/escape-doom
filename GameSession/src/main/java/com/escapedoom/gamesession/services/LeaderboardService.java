package com.escapedoom.gamesession.services;

import com.escapedoom.gamesession.data.Player;
import com.escapedoom.gamesession.data.escapeRoomDtos.LeaderboardDao;
import com.escapedoom.gamesession.repositories.SessionManagementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
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
