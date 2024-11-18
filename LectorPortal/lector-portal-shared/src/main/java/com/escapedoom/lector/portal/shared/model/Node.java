package com.escapedoom.lector.portal.shared.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Node {

    private NodeType type;

    private Position pos;

    private Object nodeInfos;

}
