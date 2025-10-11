package es.upm.iwsim22_01.commands;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;

import es.upm.iwsim22_01.models.Category;

/**
 * Clase utilitaria para convertir cadenas de texto a diferentes tipos de datos.
 * <p>
 * Proporciona métodos estáticos para convertir strings a enteros, decimales
 * y categorías, manejando los errores de conversión de forma segura.
 * </p>
 */
public class Converter {
    /**
     * Convierte una cadena de texto a un entero.
     * 
     * @param string cadena a convertir
     * @return OptionalInt con el valor convertido o vacío si la conversión falla
     */
    public static OptionalInt stringToInt(String string) {
        try {
            return OptionalInt.of(Integer.parseInt(string));
        } catch (NumberFormatException exception) {
            return OptionalInt.empty();
        }
    }

    /**
     * Convierte una cadena de texto a un número decimal.
     * 
     * @param string cadena a convertir
     * @return OptionalDouble con el valor convertido o vacío si la conversión falla
     */
    public static OptionalDouble stringToDouble(String string) {
        try {
            return OptionalDouble.of(Double.parseDouble(string));
        } catch (NumberFormatException exception) {
            return OptionalDouble.empty();
        }
    }

    /**
     * Convierte una cadena de texto a una categoría.
     * 
     * @param string cadena a convertir (se convierte a mayúsculas)
     * @return Optional con la categoría convertida o vacío si la conversión falla
     */
    public static Optional<Category> stringToCategory(String string) {
        try {
            return Optional.of(Category.valueOf(string.toUpperCase()));
        } catch (IllegalArgumentException exception) {
            return Optional.empty();
        }
    }
}
