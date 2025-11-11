package es.upm.iwsim22_01.commands;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;

import es.upm.iwsim22_01.models.Category;

public class Converter {
    public static Optional<LocalDate> stringToLocal(String date) {
        if (date == null) return Optional.empty();
        String s = date.trim();
        if (s.length() >= 2 &&
                ((s.startsWith("\"") && s.endsWith("\"")) ||
                        (s.startsWith("'")  && s.endsWith("'")))) {
            s = s.substring(1, s.length() - 1);
        }

        try {
            return Optional.of(LocalDate.parse(s, DateTimeFormatter.ISO_LOCAL_DATE));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }

    public static OptionalInt stringToInt(String string) {
        try {
            return OptionalInt.of(Integer.parseInt(string));
        } catch (NumberFormatException exception) {
            return OptionalInt.empty();
        }
    }

    public static OptionalDouble stringToDouble(String string) {
        try {
            return OptionalDouble.of(Double.parseDouble(string));
        } catch (NumberFormatException exception) {
            return OptionalDouble.empty();
        }
    }

    public static Optional<Category> stringToCategory(String string) {
        try {
            return Optional.of(Category.valueOf(string.toUpperCase()));
        } catch (IllegalArgumentException exception) {
            return Optional.empty();
        }
    }
}
