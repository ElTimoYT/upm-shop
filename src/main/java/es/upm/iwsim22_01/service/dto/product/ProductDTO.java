package es.upm.iwsim22_01.service.dto.product;

import es.upm.iwsim22_01.service.dto.product.category.Categorizable;
import es.upm.iwsim22_01.service.dto.product.category.ProductCategoryDTO;

/**
 * Clase que representa un producto unitario, especialización de AbstractProduct.
 * Incluye información sobre la categoría del producto, que determina el descuento aplicable.
 */
public class ProductDTO extends AbstractProductDTO implements Categorizable {
    private ProductCategoryDTO category;


    /**
     * Crea un producto unitario con cantidad disponible y categoría asociada.
     *
     * @param id identificador único del producto
     * @param name nombre del producto
     * @param category categoría del producto
     * @param price precio del producto
     * @param amount cantidad disponible
     */
    public ProductDTO(String id, String name, ProductCategoryDTO category, double price, int amount) {
        super(id,  name, price, amount);

        this.category = category;
    }

    /**
     * Crea un producto unitario sin control de cantidad disponible.
     *
     * @param id identificador único del producto
     * @param name nombre del producto
     * @param category categoría del producto
     * @param price precio del producto
     */
    public ProductDTO(String id, String name, ProductCategoryDTO category, double price) {
        super(id,  name, price);

        this.category = category;
    }

    /**
     * Obtiene la categoría del producto.
     *
     * @return La categoría del producto.
     */
    @Override
    public ProductCategoryDTO getCategory() {
        return category;
    }

    /**
     * Establece la categoría del producto.
     *
     * @param category Nueva categoría para el producto.
     */
    public void setCategory(ProductCategoryDTO category) {
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
