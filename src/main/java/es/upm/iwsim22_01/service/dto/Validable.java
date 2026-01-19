package es.upm.iwsim22_01.service.dto;

/**
 * Interfaz que define la capacidad de un objeto para ser validado.
 */
public interface Validable {
    /**
     * Indica si el objeto cumple las condiciones de validez definidas.
     *
     * @return true si el objeto es válido
     */
    boolean isValid();
}
