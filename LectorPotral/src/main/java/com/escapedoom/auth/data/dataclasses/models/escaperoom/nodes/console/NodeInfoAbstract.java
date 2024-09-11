package com.escapedoom.auth.data.dataclasses.models.escaperoom.nodes.console;

import com.escapedoom.auth.data.dataclasses.models.escaperoom.nodes.NodeType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Data
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
/*@JsonSubTypes({
        @JsonSubTypes.Type(value = DetailsNodeInfo.class, name = "Details" ),
        @JsonSubTypes.Type(value = ConsoleNodeInfo.class, name = "Console" ),
        @JsonSubTypes.Type(value = DataNodeInfo.class, name = "Data"),
        @JsonSubTypes.Type(value = ZoomNodeInfo.class, name = "Zoom")
})*/
public abstract class NodeInfoAbstract  {

    public NodeInfoAbstract() {
    }
}
