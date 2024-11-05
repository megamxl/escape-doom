package com.escapedoom.gamesession.rest.repositories;

import com.escapedoom.gamesession.rest.data.codeCompiling.ConsoleNodeCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeRiddleRepository extends JpaRepository<ConsoleNodeCode, Long> {
}
