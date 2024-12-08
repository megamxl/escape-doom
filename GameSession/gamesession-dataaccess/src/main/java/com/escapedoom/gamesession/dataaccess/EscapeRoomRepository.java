package com.escapedoom.gamesession.dataaccess;

import com.escapedoom.gamesession.dataaccess.entity.EscapeRoomStage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface EscapeRoomRepository extends JpaRepository<EscapeRoomStage, Long> {

    @Query(
            value = "SELECT es.stage FROM EscapeRoomStage es where es.roomId = ?1 and es.stageId = ?2"
    )
    ArrayList<Object> getEscapeRoomStageByEscaperoomIDAndStageNumber(@Param("escapeRoomId") Long escapeRoomId, @Param("stageId") Long StageId);

    Optional<EscapeRoomStage> findEscapeRoomDaoByStageIdAndRoomId(Long stageId, Long roomID);

    @Query(value = "SELECT * FROM escape_room_stage WHERE escape_roomid = :roomId AND stage_id = :stageId", nativeQuery = true)
    Optional<EscapeRoomStage> findEscapeRoomStageByStageIdAndRoomId(@Param("stageId") Long stageId, @Param("roomId") Long roomId);
    @Query(value = "SELECT es.max_stage FROM escaperoom es WHERE es.escaperoom_id = ?1", nativeQuery = true)
    Long getMaxStageByRoomId(Long escaperoomID);

}
