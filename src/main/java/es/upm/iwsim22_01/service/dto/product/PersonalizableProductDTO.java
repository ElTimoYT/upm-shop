package es.upm.iwsim22_01.service.dto.product;

/**
 * Clase que representa un producto personalizable, especialización de UnitProduct.
 * Permite definir un número máximo de caracteres para la personalización del producto.
 */
public class PersonalizableProductDTO extends UnitProductDTO {
    private int maxPers;


    /**
     * Constructor de la clase PersonalizableProduct.
     *
     * @param id Identificador único del producto.
     * @param name Nombre del producto.
     * @param category Categoría del producto.
     * @param price Precio del producto.
     * @param maxPers Número máximo de caracteres permitidos para la personalización.
     */
    public PersonalizableProductDTO(int id, String name, CategoryDTO category, double price, int maxPers){
        super(id, name, category, price);
        this.maxPers = maxPers;

    }


    /**
     * Obtiene el número máximo de caracteres permitidos para la personalización.
     *
     * @return Número máximo de caracteres para la personalización.
     */
    public int getMaxPers() {
        return maxPers;
    }

    /**
     * Devuelve una representación en cadena del producto personalizable.
     *
     * @return Cadena que representa el producto, incluyendo sus atributos.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("{class:ProductPersonalized")
                .append(", id:").append(getId())
                .append(", name:'").append(getName()).append('\'')
                .append(", category:").append(getCategory())
                .append(", price:").append(getPrice())
                .append(", maxPersonal:").append(maxPers);

        sb.append("}");
        return sb.toString();
    }

}
