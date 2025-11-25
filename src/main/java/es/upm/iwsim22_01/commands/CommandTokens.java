package es.upm.iwsim22_01.commands;

import es.upm.iwsim22_01.manager.*;
import es.upm.iwsim22_01.models.Category;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class CommandTokens {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private Iterator<String> tokens;
    private String currentToken;
    private int remainingTokens;

    public CommandTokens(List<String> tokens) {
        this.tokens = tokens.iterator();
        consumeToken();
        remainingTokens = tokens.size();
    }

    private void consumeToken() {
        currentToken = tokens.hasNext() ? tokens.next() : null;
        remainingTokens--;
    }

    private void checkToken() {
        if (currentToken == null) {
            throw new NoSuchElementException("No more tokens available");
        }
    }

    public int getRemainingTokens() {
        return remainingTokens;
    }

    public String next() {
        checkToken();

        String token = currentToken;
        consumeToken();

        return token;
    }

    public boolean hasNext() {
        return currentToken != null;
    }

    private OptionalInt tryParseInt() {
        try {
            return OptionalInt.of(Integer.parseInt(currentToken));
        } catch (NumberFormatException exception) {
            return OptionalInt.empty();
        }
    }

    public int nextInt() {
        checkToken();

        OptionalInt optionalInt = tryParseInt();
        if (optionalInt.isEmpty()) {
            throw new IllegalArgumentException("Next token is not an integer: " + currentToken);
        }

        consumeToken();
        return optionalInt.getAsInt();
    }

    public boolean hasNextInt() {
        return hasNext() && tryParseInt().isPresent();
    }

    private OptionalDouble tryParseDouble() {
        try {
            return OptionalDouble.of(Double.parseDouble(currentToken));
        } catch (NumberFormatException exception) {
            return OptionalDouble.empty();
        }
    }

    public double nextDouble() {
        checkToken();

        OptionalDouble optionalDouble = tryParseDouble();
        if (optionalDouble.isEmpty()) {
            throw new IllegalArgumentException("Next token is not a double: " + currentToken);
        }

        consumeToken();
        return optionalDouble.getAsDouble();
    }

    public boolean hasNextDouble() {
        return hasNext() && tryParseDouble().isPresent();
    }

    private Optional<Category> tryParseCategory() {
        try {
            String normalized = currentToken.trim().toUpperCase();
            return Optional.of(Category.valueOf(normalized));
        } catch (IllegalArgumentException exception) {
            return Optional.empty();
        }
    }

    public Category nextCategory() {
        checkToken();

        Optional<Category> optionalCategory = tryParseCategory();
        if (optionalCategory.isEmpty()) {
            throw new IllegalArgumentException("Next token is not a category: " + currentToken);
        }

        consumeToken();
        return optionalCategory.get();
    }

    public boolean hasNextCategory() {
        return hasNext() && tryParseCategory().isPresent();
    }

    private Optional<LocalDate> tryParseDate() {
        try {
            return Optional.of(LocalDate.parse(currentToken));
        } catch (DateTimeParseException exception) {
            return Optional.empty();
        }
    }

    public LocalDate nextDate() {
        checkToken();

        Optional<LocalDate> optionalDate = tryParseDate();
        if (optionalDate.isEmpty()) {
            throw new IllegalArgumentException("Next token is not a date: " + currentToken);
        }

        consumeToken();
        return optionalDate.get();
    }

    public boolean hasNextDate() {
        return hasNext() && tryParseDate().isPresent();
    }

    public Integer nextAsIntegerId(AbstractManager<?, Integer> manager, boolean checkIfExistsId, String messageNoToken, String messageIfNotValid) {
        if (!hasNextInt()) {
            System.out.println(messageNoToken);
            return null;
        }

        int id = nextInt();

        if (checkIfExistsId == manager.existId(id)) {
            System.out.println(messageIfNotValid);
            return null;
        }

        return id;
    }

    public String nextAsStringId(AbstractManager<?, String> manager, boolean checkIfExistsId, String messageNoToken, String messageIfNotValid) {
        if (!hasNext()) {
            System.out.println(messageNoToken);
            return null;
        }

        String id = next().toUpperCase();
        if (checkIfExistsId == manager.existId(id)) {
            System.out.println(messageIfNotValid);
            return null;
        }

        return id;
    }
}
