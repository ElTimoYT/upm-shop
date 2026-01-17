package es.upm.iwsim22_01.service.dto.product.category;

/**
 * Enumeración que representa las categorías de productos disponibles en el sistema.
 * Cada categoría está asociada a un descuento específico.
 */
public enum ServiceCategoryDTO implements Category {
    INSURANCE(),
    SHOW(),
    TRANSPORT();

    /**
     * Obtiene el descuento asociado a la categoría.
     *
     * @return El descuento asociado a la categoría.
     */
    public double getDiscount() {
        return 0.15;
    }
}
