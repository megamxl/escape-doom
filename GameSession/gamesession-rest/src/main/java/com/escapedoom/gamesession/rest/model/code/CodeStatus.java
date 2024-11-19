package com.escapedoom.gamesession.rest.model.code;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeStatus {

    private CState status;
    private String output;

}
