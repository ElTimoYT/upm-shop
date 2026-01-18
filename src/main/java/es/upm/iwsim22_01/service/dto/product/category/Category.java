package es.upm.iwsim22_01.service.dto.product.category;

/**
 * Interfaz que representa una categoría de producto con un descuento asociado.
 */
public interface Category {

    /**
     * Devuelve el descuento aplicado a la categoría.
     *
     * @return valor del descuento de la categoría
     */
    double getDiscount();
}
