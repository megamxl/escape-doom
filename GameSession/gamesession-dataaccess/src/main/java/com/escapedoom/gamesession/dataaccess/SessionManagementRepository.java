package com.escapedoom.gamesession.dataaccess;

import com.escapedoom.gamesession.dataaccess.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionManagementRepository extends JpaRepository<Player, Long> {

    Optional<Player> findPlayerByHttpSessionID(String sessionID);

    void deleteAllByEscaperoomSession(Long escaperoomSession);

    Optional<List<Player>> findAllByEscaperoomSession(Long escaperoomSession);
}
