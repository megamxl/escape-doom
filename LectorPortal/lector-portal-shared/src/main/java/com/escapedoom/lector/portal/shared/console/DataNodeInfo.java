package com.escapedoom.lector.portal.shared.console;

import jakarta.persistence.Column;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataNodeInfo extends NodeInfoAbstract {

    private Long outputID;

    private String title;

    @Column(length = 1000)
    private String desc;

    private String parameterType;

    private String exampleOutput;

    private String codeSnipped;
}
