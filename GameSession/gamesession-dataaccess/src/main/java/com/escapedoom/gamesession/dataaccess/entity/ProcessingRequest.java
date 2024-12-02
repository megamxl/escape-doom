package com.escapedoom.gamesession.dataaccess.entity;

import com.escapedoom.gamesession.shared.CompilingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "processing_request")
public class ProcessingRequest {

    @Id
    private String userID;

    @Enumerated(EnumType.STRING)
    private CompilingStatus compilingStatus;

    @Column(length = 1000)
    private String output;

}
