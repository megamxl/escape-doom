package com.escapedoom.lector.portal.dataaccess;

import com.escapedoom.lector.portal.dataaccess.entity.EscapeRoomStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepo extends JpaRepository<EscapeRoomStage, Long> {
}