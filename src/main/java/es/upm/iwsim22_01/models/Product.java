package es.upm.iwsim22_01.models;

import java.util.Objects;
import java.util.OptionalDouble;
import java.util.OptionalInt;

import es.upm.iwsim22_01.manager.ProductManager;

/**
 * Representa un producto en el sistema de tienda.
 * <p>
 * Un producto tiene un identificador único, nombre, categoría y precio.
 * La clase incluye validaciones para asegurar la integridad de los datos.
 * </p>
 */
public class Product {
    private static final int MAX_NAME_LENGTH = 100;

    private final int id;
    private String name;
    private Category category;
    private double price;

    /**
     * Constructor de la clase Product.
     * 
     * @param id identificador único del producto
     * @param name nombre del producto
     * @param category categoría del producto
     * @param price precio del producto
     * @throws IllegalArgumentException si alguno de los parámetros no es válido
     */
    public Product(int id, String name, Category category, double price) {
        if (!checkId(id)) {
            throw new IllegalArgumentException("Invalid id");
        }

        if (!checkName(name)) {
            throw new IllegalArgumentException("Invalid name");
        }

        if (!checkPrice(price)) {
            throw new IllegalArgumentException("Invalid price");
        }

        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
    }

    /**
     * Obtiene el identificador del producto.
     * 
     * @return el identificador único del producto
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene el nombre del producto.
     * 
     * @return el nombre del producto
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre del producto.
     * 
     * @param name nuevo nombre del producto
     * @throws IllegalArgumentException si el nombre no es válido
     */
    public void setName(String name) {
       if (!checkName(name)) {
           throw new IllegalArgumentException("Invalid name");
       }

        this.name = name;
    }

    /**
     * Obtiene la categoría del producto.
     * 
     * @return la categoría del producto
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Establece la categoría del producto.
     * 
     * @param category nueva categoría del producto
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * Obtiene el precio del producto.
     * 
     * @return el precio del producto
     */
    public double getPrice() {
        return price;
    }

    /**
     * Establece el precio del producto.
     * 
     * @param price nuevo precio del producto
     * @throws IllegalArgumentException si el precio no es válido
     */
    public void setPrice(double price) {
        if (!checkPrice(price)) {
            throw new IllegalArgumentException("Invalid price");
        }

        this.price = price;
    }

    /**
     * Compara este producto con otro objeto.
     * Dos productos son iguales si tienen el mismo identificador.
     * 
     * @param obj objeto a comparar
     * @return true si los productos son iguales, false en caso contrario
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Product product) {
            return product.getId() == this.getId();
        }

        return false;
    }

    /**
     * Calcula el código hash del producto basado en su identificador.
     * 
     * @return el código hash del producto
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(this.getId());
    }

    /**
     * Devuelve una representación en cadena del producto.
     * 
     * @return cadena que representa el producto con todos sus atributos
     */
    @Override
    public String toString() {
        return "Product{" +
                "class:" + this.getClass().getSimpleName() +
                ",id:" + id +
                ",name:'" + name + '\'' +
                ",category:" + category +
                ",price:" + price +
                '}';
    }

    /**
     * Verifica si un identificador es válido (no existe ya en el sistema).
     * 
     * @param id identificador a verificar
     * @return true si el identificador es válido, false en caso contrario
     */
    public static boolean checkId(int id) {
        return !ProductManager.getProductManager().existId(id);
    }

    /**
     * Verifica si un identificador opcional es válido.
     * 
     * @param id identificador opcional a verificar
     * @return true si el identificador está presente y es válido, false en caso contrario
     */
    public static boolean checkId(OptionalInt id) {
        return id.isPresent() && checkId(id.getAsInt());
    }

    /**
     * Verifica si un nombre es válido.
     * Un nombre es válido si no es null, no está en blanco y no excede la longitud máxima.
     * 
     * @param name nombre a verificar
     * @return true si el nombre es válido, false en caso contrario
     */
    public static boolean checkName(String name) {
        return name != null && !name.isBlank() && name.length() < Product.MAX_NAME_LENGTH;
    }

    /**
     * Verifica si un precio es válido.
     * Un precio es válido si es mayor que 0.
     * 
     * @param price precio a verificar
     * @return true si el precio es válido, false en caso contrario
     */
    public static boolean checkPrice(double price) {
        return price > 0;
    }

    /**
     * Verifica si un precio opcional es válido.
     * 
     * @param price precio opcional a verificar
     * @return true si el precio está presente y es válido, false en caso contrario
     */
    public static boolean checkPrice(OptionalDouble price) {
        return price.isPresent() && checkPrice(price.getAsDouble());
    }
}
