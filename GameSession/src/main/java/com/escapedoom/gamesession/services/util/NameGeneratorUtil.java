package com.escapedoom.gamesession.services.util;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@UtilityClass
public class NameGeneratorUtil {

    public String generate() {
        Random random = new Random();
        return FIRST_NAMES.get(random.nextInt(FIRST_NAMES.size())) +
                SECOND_NAMES.get(random.nextInt(SECOND_NAMES.size()));
    }
    private final List<String> FIRST_NAMES =
            Arrays.asList(
                    "Shadow", "Dark", "Crimson", "Blaze", "Frost", "Mystic", "Iron", "Silver", "Void", "Sky",
                    "Moonlight", "Blood", "Night", "Ice", "Phoenix", "Ghost", "Dragon"
            );

    private final List<String> SECOND_NAMES =
            Arrays.asList(
                    "Blade", "Night", "Viper", "Fire", "Fang", "Gaze","Fist", "Storm", "Walker", "Watcher",
                    "Assassin", "Moon", "Wing", "Slayer", "Queen", "Rider", "Bringer", "Reaper", "Slayer"
            );
}
