package com.escapedoom.gamesession.rest.repositories;

import com.escapedoom.gamesession.rest.data.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionManagementRepository extends JpaRepository<Player, Long> {

    Optional<Player> findPlayerByHttpSessionID(String sessionID);

    void deleteAllByEscaperoomSession(Long escaperoomSession);

    Optional<List<Player>> findAllByEscaperoomSession(Long escaperoomSession);

}
