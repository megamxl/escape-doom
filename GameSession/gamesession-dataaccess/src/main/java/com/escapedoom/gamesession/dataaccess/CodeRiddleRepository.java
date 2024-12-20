package com.escapedoom.gamesession.dataaccess;

import com.escapedoom.gamesession.dataaccess.entity.ConsoleNodeCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeRiddleRepository extends JpaRepository<ConsoleNodeCode, Long> {
}
