package com.escapedoom.lector.portal.shared.console;

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