package com.escapedoom.gamesession.dataaccess;

import com.escapedoom.gamesession.dataaccess.entity.OpenLobbys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface OpenLobbyRepository extends JpaRepository<OpenLobbys, Long> {

     Optional<OpenLobbys> findByLobbyId(Long Id);

}
