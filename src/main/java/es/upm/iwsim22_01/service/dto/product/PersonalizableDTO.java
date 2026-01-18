package es.upm.iwsim22_01.service.dto.product;

import es.upm.iwsim22_01.service.dto.product.category.ProductCategoryDTO;

import java.util.Arrays;
import java.util.Objects;

/**
 * Clase que representa un producto personalizable, especialización de UnitProduct.
 * Permite definir un número máximo de caracteres para la personalización del producto.
 */
public class PersonalizableDTO extends ProductDTO implements Cloneable {
    private String[] lines;

    /**
     * Crea un producto personalizable con cantidad disponible y líneas de personalización.
     *
     * @param id identificador único del producto
     * @param name nombre del producto
     * @param category categoría del producto
     * @param price precio del producto
     * @param amount cantidad disponible
     * @param lines líneas de personalización
     */
    public PersonalizableDTO(String id, String name, ProductCategoryDTO category, double price, int amount, String[] lines){
        super(id, name, category, price, amount);
        this.lines = lines;
    }

    /**
     * Crea un producto personalizable sin control de cantidad disponible.
     *
     * @param id identificador único del producto
     * @param name nombre del producto
     * @param category categoría del producto
     * @param price precio del producto
     * @param lines líneas de personalización
     */
    public PersonalizableDTO(String id, String name, ProductCategoryDTO category, double price, String[] lines){
        super(id, name, category, price);
        this.lines = lines;
    }

    /**
     * Crea un producto personalizable indicando únicamente el número máximo de líneas permitidas.
     *
     * @param id identificador único del producto
     * @param name nombre del producto
     * @param category categoría del producto
     * @param price precio del producto
     * @param maxPers número máximo de líneas de personalización
     */
    public PersonalizableDTO(String id, String name, ProductCategoryDTO category, double price, int maxPers){
        this(id, name, category, price, new String[maxPers]);
    }


    /**
     * Devuelve una copia de las líneas de personalización del producto.
     *
     * @return copia de las líneas de personalización
     */
    public String[] getLines() {
        return Arrays.copyOf(lines, lines.length);
    }

    /**
     * Establece las líneas de personalización del producto.
     *
     * @param lines nuevas líneas de personalización
     */
    public void setLines(String[] lines) {
        this.lines = Arrays.copyOf(lines, this.lines.length);
    }

    /**
     * Obtiene el número máximo de caracteres permitidos para la personalización.
     *
     * @return Número máximo de caracteres para la personalización.
     */
    public int getMaxPers() {
        return lines.length;
    }

    /**
     * Devuelve una representación en cadena del producto personalizable.
     *
     * @return Cadena que representa el producto, incluyendo sus atributos.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        String[] nonNullLines = Arrays.stream(lines)
                .filter(Objects::nonNull)
                .toArray(String[]::new);

        sb.append("Product{class:ProductPersonalized")
                .append(", id:").append(getId())
                .append(", name:'").append(getName()).append('\'')
                .append(", category:").append(getCategory())
                .append(", price:").append(getPrice())
                .append(", maxPersonal:").append(getMaxPers())
                .append(", personalizationList:").append(Arrays.toString(nonNullLines))
                .append("}");

        return sb.toString();
    }

    /**
     * Devuelve una copia profunda del producto personalizable.
     *
     * @return copia del producto personalizable
     */
    @Override
    public PersonalizableDTO clone() {
        PersonalizableDTO clone = (PersonalizableDTO) super.clone();
        clone.lines = Arrays.copyOf(lines, lines.length);
        return clone;
    }
}
