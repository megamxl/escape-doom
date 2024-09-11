package com.escapedoom.gamesession.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Player {

    @Id
    @GeneratedValue
    private int player_Id;

    private String name;

    @Column(unique = true)
    private String httpSessionID;

    private Long escaperoomSession;

    private Long escaperoomStageId;

    private Long escampeRoom_room_id;

    private Long score;

    private Long lastStageSolved;

}
