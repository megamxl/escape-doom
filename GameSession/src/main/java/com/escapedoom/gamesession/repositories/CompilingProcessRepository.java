package com.escapedoom.gamesession.repositories;

import com.escapedoom.gamesession.data.codeCompiling.ProcessingRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompilingProcessRepository extends JpaRepository<ProcessingRequest ,String> {



}
