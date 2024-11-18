package com.escapedoom.lector.portal.dataaccess;

import com.escapedoom.lector.portal.dataaccess.entity.ConsoleNodeCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeRiddleRepository extends JpaRepository<ConsoleNodeCode, Long> {
}
