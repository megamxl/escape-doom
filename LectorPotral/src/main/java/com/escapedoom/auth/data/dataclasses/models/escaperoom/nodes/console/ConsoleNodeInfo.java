package com.escapedoom.auth.data.dataclasses.models.escaperoom.nodes.console;

import jakarta.persistence.Column;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConsoleNodeInfo extends NodeInfoAbstract {

    private long outputID;

    private String codeSnipped;

    @Column(length = 500)
    private String desc;

    private String returnType;

    private String exampleInput;

    private String png;

    private String title;

}
