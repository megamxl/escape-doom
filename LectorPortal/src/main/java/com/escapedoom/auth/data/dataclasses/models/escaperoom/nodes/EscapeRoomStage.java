package com.escapedoom.auth.data.dataclasses.models.escaperoom.nodes;

import com.escapedoom.auth.data.dataclasses.models.escaperoom.Escaperoom;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
public class EscapeRoomStage {

    @Id
    @GeneratedValue
    private Long id;

    private Long stageId;

    private Long outputID;

    @JsonIgnore
    @ManyToOne( cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "escapeRoomID")
    private Escaperoom escaperoom;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<Scenes> stage;

    @Override
    public String toString() {
        return "EscapeRoomStage{" +
                "id=" + id +
                ", stage=" + stage +
                '}';
    }
}
