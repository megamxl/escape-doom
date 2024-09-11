package com.escapedoom.auth.data.dataclasses.repositories;

import com.escapedoom.auth.data.dataclasses.models.escaperoom.ConsoleNodeCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeRiddleRepository extends JpaRepository<ConsoleNodeCode, Long> {
}
