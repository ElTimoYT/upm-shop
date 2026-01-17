package es.upm.iwsim22_01.commands;

import es.upm.iwsim22_01.service.dto.product.category.ProductCategoryDTO;
import es.upm.iwsim22_01.service.dto.product.category.ServiceCategoryDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Clase que gestiona una secuencia de tokens y proporciona utilidades para obtenerlos
 * en distintos tipos (cadena, entero, doble, fecha, categoría, etc.).
 * Cada llamada a los métodos next* avanza automáticamente al siguiente token.
 */
public class CommandTokens {
    private Iterator<String> tokens;
    private String currentToken;
    private int remainingTokens;

    /**
     * Construye un nuevo conjunto de tokens basado en una lista.
     * El iterador se inicializa automáticamente y posiciona el primer token como actual.
     *
     * @param tokens lista de tokens en orden secuencial
     */
    public CommandTokens(List<String> tokens) {
        this.tokens = tokens.iterator();
        consumeToken();
        remainingTokens = tokens.size();
    }

    /**
     * Avanza al siguiente token si existe, o establece null si no quedan más.
     */
    private void consumeToken() {
        currentToken = tokens.hasNext() ? tokens.next() : null;
        remainingTokens--;
    }

    /**
     * Comprueba si existe un token actual. Si no, lanza una excepción.
     *
     * @throws NoSuchElementException si no quedan tokens
     */
    private void checkToken() {
        if (currentToken == null) {
            throw new NoSuchElementException("No more tokens available");
        }
    }

    /**
     * Devuelve la cantidad de tokens restantes por consumir.
     *
     * @return número de tokens pendientes
     */
    public int getRemainingTokens() {
        return remainingTokens;
    }

    /**
     * Devuelve el token actual como cadena y avanza al siguiente.
     *
     * @return token actual
     * @throws NoSuchElementException si no quedan tokens
     */
    public String next() {
        checkToken();

        String token = currentToken;
        consumeToken();

        return token;
    }

    /**
     * Indica si existe un token disponible.
     *
     * @return true si hay un token pendiente, false en caso contrario
     */
    public boolean hasNext() {
        return currentToken != null;
    }

    /**
     * Intenta interpretar el token actual como entero.
     *
     * @return OptionalInt con el entero si es válido, vacío si no
     */
    private OptionalInt tryParseInt() {
        try {
            return OptionalInt.of(Integer.parseInt(currentToken));
        } catch (NumberFormatException exception) {
            return OptionalInt.empty();
        }
    }

    /**
     * Devuelve el token actual como entero y avanza al siguiente.
     *
     * @return entero representado por el token actual
     * @throws IllegalArgumentException si el token no es un entero válido
     */
    public int nextInt() {
        checkToken();

        OptionalInt optionalInt = tryParseInt();
        if (optionalInt.isEmpty()) {
            throw new IllegalArgumentException("Next token is not an integer: " + currentToken);
        }

        consumeToken();
        return optionalInt.getAsInt();
    }

    /**
     * Indica si el token actual es un entero válido.
     *
     * @return true si puede convertirse a entero, false en caso contrario
     */
    public boolean hasNextInt() {
        return hasNext() && tryParseInt().isPresent();
    }

    /**
     * Intenta interpretar el token actual como double.
     *
     * @return OptionalDouble con el valor si es válido, vacío si no
     */
    private OptionalDouble tryParseDouble() {
        try {
            return OptionalDouble.of(Double.parseDouble(currentToken));
        } catch (NumberFormatException exception) {
            return OptionalDouble.empty();
        }
    }

    /**
     * Devuelve el token actual como double y avanza al siguiente.
     *
     * @return double representado por el token actual
     * @throws IllegalArgumentException si el token no es un double válido
     */
    public double nextDouble() {
        checkToken();

        OptionalDouble optionalDouble = tryParseDouble();
        if (optionalDouble.isEmpty()) {
            throw new IllegalArgumentException("Next token is not a double: " + currentToken);
        }

        consumeToken();
        return optionalDouble.getAsDouble();
    }

    /**
     * Indica si el token actual puede convertirse a double.
     *
     * @return true si el token es un double válido, false en caso contrario
     */
    public boolean hasNextDouble() {
        return hasNext() && tryParseDouble().isPresent();
    }

    /**
     * Intenta interpretar el token actual como categoría del enum Category.
     * El valor se normaliza a mayúsculas antes de comparar.
     *
     * @return Optional con la categoría válida, vacío si no coincide con ninguna
     */
    private Optional<ProductCategoryDTO> tryParseProductCategory() {
        try {
            String normalized = currentToken.trim().toUpperCase();
            return Optional.of(ProductCategoryDTO.valueOf(normalized));
        } catch (IllegalArgumentException exception) {
            return Optional.empty();
        }
    }

    /**
     * Devuelve el token actual como categoría del enum Category y avanza al siguiente.
     *
     * @return categoría representada por el token actual
     * @throws IllegalArgumentException si el token no es una categoría válida
     */
    public ProductCategoryDTO nextProductCategory() {
        checkToken();

        Optional<ProductCategoryDTO> optionalCategory = tryParseProductCategory();
        if (optionalCategory.isEmpty()) {
            throw new IllegalArgumentException("Next token is not a product category: " + currentToken);
        }

        consumeToken();
        return optionalCategory.get();
    }

    /**
     * Indica si el token actual es una categoría válida.
     *
     * @return true si el token es una categoría del enum Category, false en caso contrario
     */
    public boolean hasNextProductCategory() {
        return hasNext() && tryParseProductCategory().isPresent();
    }

    /**
     * Intenta interpretar el token actual como categoría del enum Category.
     * El valor se normaliza a mayúsculas antes de comparar.
     *
     * @return Optional con la categoría válida, vacío si no coincide con ninguna
     */
    private Optional<ServiceCategoryDTO> tryParseServiceCategory() {
        try {
            String normalized = currentToken.trim().toUpperCase();
            return Optional.of(ServiceCategoryDTO.valueOf(normalized));
        } catch (IllegalArgumentException exception) {
            return Optional.empty();
        }
    }

    /**
     * Devuelve el token actual como categoría del enum Category y avanza al siguiente.
     *
     * @return categoría representada por el token actual
     * @throws IllegalArgumentException si el token no es una categoría válida
     */
    public ServiceCategoryDTO nextServiceCategory() {
        checkToken();

        Optional<ServiceCategoryDTO> optionalCategory = tryParseServiceCategory();
        if (optionalCategory.isEmpty()) {
            throw new IllegalArgumentException("Next token is not a service category: " + currentToken);
        }

        consumeToken();
        return optionalCategory.get();
    }

    /**
     * Indica si el token actual es una categoría válida.
     *
     * @return true si el token es una categoría del enum Category, false en caso contrario
     */
    public boolean hasNextServiceCategory() {
        return hasNext() && tryParseServiceCategory().isPresent();
    }

    /**
     * Intenta interpretar el token actual como fecha con formato ISO (yyyy-MM-dd).
     *
     * @return Optional con la fecha válida, vacío si no lo es
     */
    private Optional<LocalDate> tryParseDate() {
        try {
            return Optional.of(LocalDate.parse(currentToken));
        } catch (DateTimeParseException exception) {
            return Optional.empty();
        }
    }

    /**
     * Devuelve el token actual como fecha convertida a LocalDateTime al inicio del día
     * y avanza al siguiente token.
     *
     * @return fecha representada por el token actual
     * @throws IllegalArgumentException si el token no es una fecha válida
     */
    public LocalDateTime nextDate() {
        checkToken();

        Optional<LocalDate> optionalDate = tryParseDate();
        if (optionalDate.isEmpty()) {
            throw new IllegalArgumentException("Next token is not a date: " + currentToken);
        }

        consumeToken();
        return optionalDate.get().atStartOfDay();
    }

    /**
     * Indica si el token actual puede interpretarse como una fecha válida (yyyy-MM-dd).
     *
     * @return true si es una fecha válida, false en caso contrario
     */
    public boolean hasNextDate() {
        return hasNext() && tryParseDate().isPresent();
    }
}
