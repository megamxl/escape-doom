package com.escapedoom.lector.portal.dataaccess.entity;

import com.escapedoom.lector.portal.shared.model.Scenes;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "escape_room_stage")
public class EscapeRoomStage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "escape_room_stage_seq")
    @SequenceGenerator(name = "escape_room_stage_seq", sequenceName = "escape_room_stage_seq", allocationSize = 50)
    private Long id;

    @Column(name = "stage_id")
    private Long stageId;

    @Column(name = "outputid")
    private Long outputID;

    /** uncomment if facing problems **/
    //@JsonIgnore
    //@ManyToOne( cascade = CascadeType.ALL)
    //@Fetch(FetchMode.JOIN)
    //@JoinColumn(name = "escapeRoomID")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "escape_roomid")
    private Escaperoom escaperoom;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<Scenes> stage;
}
