package com.escapedoom.auth.data.dataclasses.models.escaperoom.nodes.console;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ZoomNodeInfo extends NodeInfoAbstract {

    private String desc;

    private String png;

    private String title;

}