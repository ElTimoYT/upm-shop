package es.upm.iwsim22_01.service.dto.product.category;


/**
 * Interfaz que define la capacidad de un objeto para pertenecer a una categoría.
 */
public interface Categorizable {
    /**
     * Devuelve la categoría asociada al objeto.
     *
     * @return categoría del objeto
     */
    Category getCategory();
}
