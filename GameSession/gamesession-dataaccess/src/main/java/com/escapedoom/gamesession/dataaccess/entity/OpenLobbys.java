package com.escapedoom.gamesession.dataaccess.entity;

import com.escapedoom.gamesession.shared.EscapeRoomState;
import jakarta.persistence.*;
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
@Table(name = "open_lobbys")
public class OpenLobbys {

    //TODO THIS FILED IS THE ROOM PIN MAKE IST MORE CRACY NOT PROCEDUARAL
    @Id
    @GeneratedValue
    private Long lobbyId;

    private Long escaperoom_escaperoom_id;

    private Long user_user_id;

    @Enumerated(EnumType.STRING)
    private EscapeRoomState state;

    private LocalDateTime endTime;

    private LocalDateTime startTime;

}
