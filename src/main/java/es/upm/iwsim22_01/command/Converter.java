package es.upm.iwsim22_01.command;

import es.upm.iwsim22_01.models.Category;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;

public class Converter {
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
