package com.escapedoom.lector.portal.shared.console;

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
