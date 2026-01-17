package es.upm.iwsim22_01.service.dto.product;

/**
 * Enumeración que representa las categorías de productos disponibles en el sistema.
 * Cada categoría está asociada a un descuento específico.
 */
public enum CategoryDTO {
    MERCH(0),
    STATIONARY(0.05),
    CLOTHES(0.07),
    BOOK(0.1),
    ELECTRONICS(0.03),
    INSURANCE(0.15),
    SHOW(0.15),
    TRANSPORT(0.15);


    private final double discount;

    /**
     * Constructor de la enumeración.
     *
     * @param discount Descuento asociado a la categoría.
     */
    CategoryDTO(double discount) {
        this.discount = discount;
    }

    /**
     * Obtiene el descuento asociado a la categoría.
     *
     * @return El descuento asociado a la categoría.
     */
    public double getDiscount() {
        return this.discount;
    }
}
