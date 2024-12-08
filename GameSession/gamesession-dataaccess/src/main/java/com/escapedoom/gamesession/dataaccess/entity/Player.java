package com.escapedoom.gamesession.dataaccess.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "player")
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
