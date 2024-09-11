package com.escapedoom.auth.data.dataclasses.models.escaperoom.nodes.console;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailsNodeInfo extends NodeInfoAbstract {

    private String desc;

    @Column(length = 400)
    private String png;

    private String title;

}
