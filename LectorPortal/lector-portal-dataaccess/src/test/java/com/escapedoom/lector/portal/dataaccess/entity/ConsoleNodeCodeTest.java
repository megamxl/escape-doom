package com.escapedoom.lector.portal.dataaccess.entity;

import com.escapedoom.lector.portal.shared.model.CodingLanguage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConsoleNodeCodeTest {

    @Test
    void testConsoleNodeCodeCreation() {
        ConsoleNodeCode code = ConsoleNodeCode.builder()
                .language(CodingLanguage.JAVA)
                .functionSignature("public void testMethod()")
                .input("int x = 5;")
                .variableName("x")
                .expectedOutput("5")
                .build();

        assertThat(code.getLanguage()).isEqualTo(CodingLanguage.JAVA);
        assertThat(code.getFunctionSignature()).isEqualTo("public void testMethod()");
        assertThat(code.getInput()).isEqualTo("int x = 5;");
        assertThat(code.getVariableName()).isEqualTo("x");
        assertThat(code.getExpectedOutput()).isEqualTo("5");
    }

    @Test
    void testConsoleNodeCodeWithNullValues() {
        ConsoleNodeCode code = new ConsoleNodeCode();

        assertThat(code.getLanguage()).isNull();
        assertThat(code.getFunctionSignature()).isNull();
        assertThat(code.getInput()).isNull();
        assertThat(code.getVariableName()).isNull();
        assertThat(code.getExpectedOutput()).isNull();
    }
}
