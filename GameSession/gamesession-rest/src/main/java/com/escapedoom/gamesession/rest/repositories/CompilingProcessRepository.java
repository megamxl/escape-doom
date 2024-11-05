package com.escapedoom.gamesession.rest.repositories;

import com.escapedoom.gamesession.rest.data.codeCompiling.ProcessingRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompilingProcessRepository extends JpaRepository<ProcessingRequest ,String> {



}
