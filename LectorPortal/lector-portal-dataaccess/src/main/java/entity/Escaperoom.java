package entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@Entity
@ToString
@NoArgsConstructor
public class Escaperoom {
    @Id
    @GeneratedValue
    private Long escaperoom_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String name;

    private String topic;

    private Integer time;

    @OneToMany(mappedBy = "escaperoom", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<EscapeRoomStage> escapeRoomStages;

    private Long maxStage;

    public Escaperoom(Long escaperoom_id, User user, String name, String topic, Integer time, List<EscapeRoomStage> escapeRoomStages, Long maxStage) {
        this.escaperoom_id = escaperoom_id;
        this.user = user;
        this.name = name;
        this.topic = topic;
        this.time = time;
        this.escapeRoomStages = escapeRoomStages;
        this.maxStage = (long) escapeRoomStages.size();
    }

    public void setEscapeRoomStages(List<EscapeRoomStage> escapeRoomStages) {
        this.setMaxStage((long) escapeRoomStages.size()+1);
        this.escapeRoomStages = escapeRoomStages;
    }
}
