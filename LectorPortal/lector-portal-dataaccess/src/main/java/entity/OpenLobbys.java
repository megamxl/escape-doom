package entity;

import jakarta.persistence.*;
import lombok.*;
import types.EscapeRoomState;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
public class OpenLobbys {

    @Id
    @GeneratedValue
    private Long lobby_Id;

    @OneToOne
    private Escaperoom escaperoom;

    @OneToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private EscapeRoomState state;

    private LocalDateTime endTime;

    private LocalDateTime startTime;
}
