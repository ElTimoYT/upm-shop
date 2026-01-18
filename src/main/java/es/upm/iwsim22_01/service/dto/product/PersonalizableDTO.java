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

    public PersonalizableDTO(String id, String name, ProductCategoryDTO category, double price, int amount, String[] lines){
        super(id, name, category, price, amount);
        this.lines = lines;
    }

    public PersonalizableDTO(String id, String name, ProductCategoryDTO category, double price, String[] lines){
        super(id, name, category, price);
        this.lines = lines;
    }

    /**
     * Constructor de la clase PersonalizableProduct.
     *
     * @param id Identificador único del producto.
     * @param name Nombre del producto.
     * @param category Categoría del producto.
     * @param price Precio del producto.
     * @param maxPers Número máximo de caracteres permitidos para la personalización.
     */
    public PersonalizableDTO(String id, String name, ProductCategoryDTO category, double price, int maxPers){
        this(id, name, category, price, new String[maxPers]);
    }

    public String[] getLines() {
        return Arrays.copyOf(lines, lines.length);
    }

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

    @Override
    public PersonalizableDTO clone() {
        PersonalizableDTO clone = (PersonalizableDTO) super.clone();
        clone.lines = Arrays.copyOf(lines, lines.length);
        return clone;
    }
}
