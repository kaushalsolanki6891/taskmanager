package com.application.taskmanager.util;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class EnumParser {

    public static <T extends Enum<T>> List<T> parseCsv(
            String csv,
            Class<T> enumClass
    ) {

        if (csv == null || csv.isBlank()) {
            return List.of();
        }

        return Arrays.stream(csv.split(","))
                .map(value -> value.trim().toUpperCase(Locale.ROOT))
                .map(value -> Enum.valueOf(enumClass, value))
                .collect(Collectors.toList());
    }
}