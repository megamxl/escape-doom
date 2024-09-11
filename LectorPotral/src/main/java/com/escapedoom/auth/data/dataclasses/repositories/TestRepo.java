package com.escapedoom.auth.data.dataclasses.repositories;

import com.escapedoom.auth.data.dataclasses.models.escaperoom.nodes.EscapeRoomStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepo extends JpaRepository<EscapeRoomStage, Long> {
}
