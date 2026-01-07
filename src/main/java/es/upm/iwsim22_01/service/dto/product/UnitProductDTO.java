package es.upm.iwsim22_01.service.dto.product;

/**
 * Clase que representa un producto unitario, especialización de AbstractProduct.
 * Incluye información sobre la categoría del producto, que determina el descuento aplicable.
 */
public class UnitProductDTO extends AbstractProductDTO {
    private CategoryDTO category;

    public UnitProductDTO(int id, String name, CategoryDTO category, double price, int amount) {
        super(id,  name, price, amount);

        this.category = category;
    }

    /**
     * Constructor de la clase UnitProduct.
     *
     * @param id Identificador único del producto.
     * @param name Nombre del producto.
     * @param category Categoría del producto.
     * @param price Precio del producto.
     */
    public UnitProductDTO(int id, String name, CategoryDTO category, double price) {
        super(id,  name, price);

        this.category = category;
    }

    /**
     * Obtiene la categoría del producto.
     *
     * @return La categoría del producto.
     */
    public CategoryDTO getCategory() {
        return category;
    }

    /**
     * Establece la categoría del producto.
     *
     * @param category Nueva categoría para el producto.
     */
    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    /**
     * Devuelve una representación en cadena del producto unitario.
     *
     * @return Cadena que representa el producto, incluyendo su clase, identificador, nombre, precio y categoría.
     */
    @Override
    public String toString() {
        return "Product{" +
                "class:" + this.getClass().getSimpleName() +
                ",id:" + id +
                ",name:'" + name + '\'' +
                ",price:" + price +
                ",Category: "+category+
                '}';

    }
}
