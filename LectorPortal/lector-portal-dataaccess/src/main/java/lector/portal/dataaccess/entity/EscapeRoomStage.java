package lector.portal.dataaccess.entity;

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
}
