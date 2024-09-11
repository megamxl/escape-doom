package com.escapedoom.gamesession.repositories;

import com.escapedoom.gamesession.data.EscapeRoomDao;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface EscapeRoomRepo extends JpaRepository<EscapeRoomDao, Long> {

    @Query(
            value = "SELECT es.stage FROM EscapeRoomDao es where es.roomId = ?1 and es.stageId = ?2"
    )
    ArrayList<Object> getEscapeRoomStageByEscaperoomIDAndStageNumber(@Param("escapeRoomId") Long escapeRoomId, @Param("stageId") Long StageId);

    Optional<EscapeRoomDao> findEscapeRoomDaoByStageIdAndRoomId(Long stageId, Long roomID);

    @Query(value = "SELECT es.max_stage FROM escaperoom es WHERE es.escaperoom_id = ?1", nativeQuery = true)
    Long getMaxStage(Long escaperoomID);

}
