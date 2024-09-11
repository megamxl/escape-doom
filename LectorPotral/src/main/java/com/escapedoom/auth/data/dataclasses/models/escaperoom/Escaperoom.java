package com.escapedoom.auth.data.dataclasses.models.escaperoom;

import com.escapedoom.auth.data.dataclasses.models.escaperoom.nodes.EscapeRoomStage;
import com.escapedoom.auth.data.dataclasses.models.user.User;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@NoArgsConstructor
@Builder
@Entity
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

    @Override
    public String toString() {
        return "Escaperoom{" +
                "escaperoom_id=" + escaperoom_id +
                ", user=" + user +
                ", name='" + name + '\'' +
                ", topic='" + topic + '\'' +
                ", time=" + time +
                '}';
    }


    public Long getEscaperoom_id() {
        return escaperoom_id;
    }

    public void setEscaperoom_id(Long escaperoom_id) {
        this.escaperoom_id = escaperoom_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public List<EscapeRoomStage> getEscapeRoomStages() {
        return escapeRoomStages;
    }

    public void setEscapeRoomStages(List<EscapeRoomStage> escapeRoomStages) {
        this.setMaxStage((long) escapeRoomStages.size()+1);
        this.escapeRoomStages = escapeRoomStages;
    }

    public Long getMaxStage() {
        return maxStage;
    }

    public void setMaxStage(Long maxStage) {
        this.maxStage = maxStage;
    }
}
