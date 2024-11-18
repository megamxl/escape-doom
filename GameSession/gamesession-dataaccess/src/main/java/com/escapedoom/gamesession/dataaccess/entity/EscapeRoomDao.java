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
public class EscapeRoomDao {

    @Id
    @Column(name = "id")
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
