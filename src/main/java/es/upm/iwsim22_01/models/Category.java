package es.upm.iwsim22_01.models;

/**
 * Enum que representa las diferentes categorías de productos disponibles en la tienda.
 * <p>
 * Cada categoría tiene asociado un descuento que se aplica cuando hay 2 o más productos
 * de la misma categoría en el ticket.
 * </p>
 */
public enum Category {
    MERCH(0),
    STATIONARY(0.05),
    CLOTHES(0.07),
    BOOK(0.1),
    ELECTRONICS(0.03);

    private final double discount;

    Category(double discount) {
        this.discount = discount;
    }

    /**
     * Obtiene el descuento asociado a la categoría.
     * 
     * @return el descuento como valor decimal (ej: 0.05 para 5%)
     */
    public double getDiscount() {
        return this.discount;
    }
}
