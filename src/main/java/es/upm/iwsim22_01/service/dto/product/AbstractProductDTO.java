package es.upm.iwsim22_01.service.dto.product;

import java.util.Objects;

/**
 * Clase abstracta que representa un producto genérico en el sistema.
 * Define los atributos y métodos básicos comunes a todos los tipos de productos.
 */
public abstract class AbstractProductDTO implements Cloneable {
    protected final String id;
    protected String name;
    protected double price;
    protected int amount;

    /**
     * Crea un producto con identificador, nombre, precio y cantidad inicial.
     *
     * @param id identificador único del producto
     * @param name nombre del producto
     * @param price precio del producto
     * @param amount cantidad inicial disponible
     */
    public AbstractProductDTO(String id, String name, double price, int amount) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.amount = amount;
    }

    /**
     * Crea un producto con identificador, nombre y precio sin cantidad inicial.
     *
     * @param id identificador único del producto
     * @param name nombre del producto
     * @param price precio del producto
     */
    public AbstractProductDTO(String id, String name, double price) {
        this(id, name, price, 0);
    }

    /**
     * Devuelve el identificador único del producto.
     *
     * @return identificador del producto
     */
    public String getId() {
        return id;
    }

    /**
     * Devuelve el nombre del producto.
     *
     * @return nombre del producto
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre del producto.
     *
     * @param name nuevo nombre del producto
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtiene el precio del producto.
     *
     * @return El precio del producto.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Establece el precio del producto.
     *
     * @param price Nuevo precio para el producto.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Devuelve la cantidad disponible del producto.
     *
     * @return cantidad disponible
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Incrementa la cantidad disponible del producto.
     *
     * @param amountToAdd cantidad a añadir
     */
    public void addAmount(int amountToAdd) {
        amount += amountToAdd;
    }

    /**
     * Compara este producto con otro objeto para determinar si son iguales.
     * Dos productos se consideran iguales si tienen el mismo identificador.
     *
     * @param obj Objeto con el que comparar.
     * @return true si los objetos son iguales, false en caso contrario.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;

        if (obj instanceof AbstractProductDTO product) {
            return product.id == this.id;
        }

        return false;
    }

    /**
     * Genera un código hash para el producto basado en su identificador.
     *
     * @return Código hash del producto.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(this.getId());
    }

    /**
     * Devuelve una representación en cadena del producto.
     *
     * @return Cadena que representa el producto, incluyendo su clase, identificador, nombre y precio.
     */
    @Override
    public String toString() {
        return "Product{" +
                "class:" + this.getClass().getSimpleName() +
                ",id:" + id +
                ",name:'" + name + '\'' +
                ",price:" + price +
                '}';
    }

    @Override
    public AbstractProductDTO clone() {
        try {
            return (AbstractProductDTO) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
