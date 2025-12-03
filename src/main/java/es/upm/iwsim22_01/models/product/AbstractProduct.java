package es.upm.iwsim22_01.models.product;

import java.util.Objects;

/**
 * Clase abstracta que representa un producto genérico en el sistema.
 * Define los atributos y métodos básicos comunes a todos los tipos de productos.
 */
public abstract class AbstractProduct {
    protected final int id;
    protected String name;
    protected double price;

    /**
     * Constructor de la clase AbstractProduct.
     *
     * @param id Identificador único del producto.
     * @param name Nombre del producto.
     * @param price Precio del producto.
     */
    public AbstractProduct(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    /**
     * Obtiene el identificador único del producto.
     *
     * @return El identificador del producto.
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene el nombre del producto.
     *
     * @return El nombre del producto.
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre del producto.
     *
     * @param name Nuevo nombre para el producto.
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
     * Compara este producto con otro objeto para determinar si son iguales.
     * Dos productos se consideran iguales si tienen el mismo identificador.
     *
     * @param obj Objeto con el que comparar.
     * @return true si los objetos son iguales, false en caso contrario.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;

        if (obj instanceof AbstractProduct product) {
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
}
