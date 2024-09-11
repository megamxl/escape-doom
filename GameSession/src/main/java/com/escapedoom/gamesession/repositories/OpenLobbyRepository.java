package com.escapedoom.gamesession.repositories;

import com.escapedoom.gamesession.data.OpenLobbys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface OpenLobbyRepository extends JpaRepository<OpenLobbys, Long> {

     Optional<OpenLobbys> findByLobbyId(Long Id);

}
