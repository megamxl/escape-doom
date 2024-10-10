package com.escapedoom.gamesession.services.util;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NameGeneratorUtilTest {

    private static List<String> firstNames;
    private static List<String> secondNames;

    @BeforeAll
    static void setUp() throws IOException {
        firstNames = Files.readAllLines(Path.of("src/test/resources/first_names.txt"));
        secondNames = Files.readAllLines(Path.of("src/test/resources/second_names.txt"));
    }

    @RepeatedTest(10)
    void testGeneratedFirstNameIsValid() {
        String generatedName = NameGeneratorUtil.generate();
        String firstNamePart = generatedName.split("(?=[A-Z])")[0];

        assertThat(firstNames)
                .as("Generated first name should match one from the list.")
                .contains(firstNamePart);
    }

    @RepeatedTest(10)
    void testGeneratedLastNameIsValid() {
        String generatedName = NameGeneratorUtil.generate();
        String lastNamePart = generatedName.split("(?=[A-Z])")[1];

        assertThat(secondNames)
                .as("Generated last name should match one from the list.")
                .contains(lastNamePart);
    }
}
