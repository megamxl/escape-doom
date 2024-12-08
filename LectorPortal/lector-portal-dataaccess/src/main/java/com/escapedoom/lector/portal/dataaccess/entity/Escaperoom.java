package com.escapedoom.lector.portal.dataaccess.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Builder
@Entity
@ToString
public class Escaperoom {
    @Id
    @GeneratedValue
    @Column(name = "escaperoom_id")
    private Long escapeRoomId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String name;

    private String topic;

    private Integer time;

    @OneToMany(mappedBy = "escaperoom", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<EscapeRoomStage> escapeRoomStages;

    @Column(name = "max_stage")
    private Long maxStage;

    public Escaperoom(Long escapeRoomId, User user, String name, String topic, Integer time, List<EscapeRoomStage> escapeRoomStages, Long maxStage) {
        this.escapeRoomId = escapeRoomId;
        this.user = user;
        this.name = name;
        this.topic = topic;
        this.time = time;
        this.escapeRoomStages = escapeRoomStages != null ? escapeRoomStages : new ArrayList<>();
        this.maxStage = (long) this.escapeRoomStages.size();
    }

    public void setEscapeRoomId(Long escaperoom_id) {
        this.escapeRoomId = escaperoom_id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public void setEscapeRoomStages(List<EscapeRoomStage> escapeRoomStages) {
        this.setMaxStage((long) escapeRoomStages.size()+1);
        this.escapeRoomStages = escapeRoomStages;
    }

    public void setMaxStage(Long maxStage) {
        this.maxStage = maxStage;
    }
}