package com.escapedoom.lector.portal.shared.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum NodeType {
    CONSOLE,
    DETAILS,
    STORY,
    ZOOM;

    @JsonCreator
    public static NodeType fromString(String value) {
        for (NodeType type : NodeType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid NodeType value: " + value);
    }

    @JsonValue
    public String toJson() {
        return this.name();
    }
}
