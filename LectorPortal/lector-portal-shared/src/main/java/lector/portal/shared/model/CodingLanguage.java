package lector.portal.shared.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CodingLanguage {
    JAVA,
    JAVASCRIPT,
    PYTHON;

    @JsonCreator
    public static CodingLanguage fromString(String value) {
        for (CodingLanguage type : CodingLanguage.values()) {
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
