package com.escapedoom.gamesession.dataaccess;

import com.escapedoom.gamesession.dataaccess.entity.ProcessingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompilationRepository extends JpaRepository<ProcessingRequest,String> {
}
