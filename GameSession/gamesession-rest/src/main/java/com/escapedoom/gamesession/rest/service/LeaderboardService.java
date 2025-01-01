package com.escapedoom.gamesession.rest.service;

import com.escapedoom.gamesession.dataaccess.SessionManagementRepository;
import com.escapedoom.gamesession.dataaccess.entity.Player;
import com.escapedoom.gamesession.rest.model.leaderboard.LeaderboardEntry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeaderboardService {

    private final SessionManagementRepository repository;

    public List<LeaderboardEntry> getScoreBoard(Long escaperoomID) {
        List<LeaderboardEntry> playerScores = new ArrayList<>();
        try {
            Optional<List<Player>> allPlayerOfEscaperoom = repository.findAllByEscaperoomSession(escaperoomID);
            if (allPlayerOfEscaperoom.isPresent()) {
                log.debug("Found {} players in Leaderboard for ER-ID {}", allPlayerOfEscaperoom.get().size(), escaperoomID);
                for (Player p : allPlayerOfEscaperoom.get()) {
                    playerScores.add(new LeaderboardEntry(p));
                }
            } else {
                log.info("The EscapeRoom with the ID {} was not found.", escaperoomID);
            }
        } catch (Exception ex) {
            log.error("Error while retrieving the leaderboard for EscapeRoom ID: {}", escaperoomID, ex);
            throw new RuntimeException("Failed to retrieve the leaderboard for EscapeRoom ID: " + escaperoomID, ex);
        }
        return playerScores;
    }
}
