package com.escapedoom.gamesession.repositories;

import com.escapedoom.gamesession.data.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SessionManagementRepository extends JpaRepository<Player, Long> {

    Optional<Player> findPlayerByHttpSessionID(String sessionID);

    void deleteAllByEscaperoomSession(Long escaperoomSession);

    Optional<List<Player>> findAllByEscaperoomSession(Long escaperoomSession);

}
