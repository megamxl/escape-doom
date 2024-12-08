package com.escapedoom.gamesession.dataaccess.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "escape_room_stage")
public class EscapeRoomStage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "escape_room_stage_seq")
    @SequenceGenerator(name = "escape_room_stage_seq", sequenceName = "escape_room_stage_seq", allocationSize = 50)
    private Long id;

    @Column(name = "escape_roomid")
    private Long roomId;

    @Column(name = "stage_id")
    private Long stageId;

    @Column(name = "stage")
    @JdbcTypeCode(SqlTypes.JSON)
    private String stage;

    private Long outputID;
}
