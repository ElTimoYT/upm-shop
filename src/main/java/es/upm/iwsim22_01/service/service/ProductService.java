package es.upm.iwsim22_01.service.service;

import es.upm.iwsim22_01.service.dto.product.*;

import java.time.LocalDateTime;

/**
 * Gestor de productos, encargado de la creación, validación y registro de instancias de {@link AbstractProductDTO}.
 * Permite añadir diferentes tipos de productos al sistema, validando sus parámetros y restricciones.
 */
public class ProductService extends AbstractService<AbstractProductDTO, Integer> {
    private final static int MAX_PRODUCTS = 200, MAX_NAME_LENGTH = 100;

    /**
     * Añade un producto unitario al sistema.
     *
     * @param id       Identificador único del producto.
     * @param name     Nombre del producto (no puede ser nulo, vacío o superar {@value #MAX_NAME_LENGTH} caracteres).
     * @param category Categoría del producto.
     * @param price    Precio del producto (debe ser mayor que 0).
     * @return La instancia de {@link AbstractProductDTO} creada.
     * @throws IllegalArgumentException Si el precio es negativo, el nombre no es válido o el producto ya existe.
     * @throws RuntimeException Si se ha alcanzado el número máximo de productos permitidos.
     */
    public AbstractProductDTO addProduct(int id, String name, CategoryDTO category, double price) {
        if (!isPriceValid(price)) throw new IllegalArgumentException("Product price " + price + " cannot be negative.");
        if (!isNameValid(name)) throw new IllegalArgumentException("Product name \"" + price + "\" is invalid.");
        if (isProductListFull()) throw new RuntimeException("Product cannot be added, there are " + MAX_PRODUCTS + " or more products.");

        AbstractProductDTO product = new UnitProductDTO(id, name, category, price);
        add(product, id);

        return product;
    }

    /**
     * Añade un producto personalizable al sistema.
     *
     * @param id           Identificador único del producto.
     * @param name         Nombre del producto (no puede ser nulo, vacío o superar {@value #MAX_NAME_LENGTH} caracteres).
     * @param category     Categoría del producto.
     * @param price        Precio del producto (debe ser mayor que 0).
     * @param maxText      Número máximo de caracteres personalizables (debe ser mayor o igual a 1).
     * @return La instancia de {@link AbstractProductDTO} creada.
     * @throws IllegalArgumentException Si el precio es negativo, el nombre no es válido, el producto ya existe o maxText es menor que 1.
     * @throws RuntimeException Si se ha alcanzado el número máximo de productos permitidos.
     */
    public AbstractProductDTO addCustomizableProduct(int id, String name, CategoryDTO category, double price, int maxText){
        if (!isPriceValid(price)) throw new IllegalArgumentException("Product price " + price + " cannot be negative.");
        if (!isNameValid(name)) throw new IllegalArgumentException("Product name \"" + price + "\" is invalid.");
        if (isProductListFull()) throw new RuntimeException("Product cannot be added, there are " + MAX_PRODUCTS + " or more products.");
        if (maxText < 1) throw new IllegalArgumentException("Max pers " + maxText + " cannot be less than 1.");
        if (existId(id)) throw new IllegalArgumentException("Product id " + id + " already exists(cannot convert basic to customizable).");

        AbstractProductDTO product = new PersonalizableProductDTO(id, name, category, price, maxText);
        add(product, id);

        return  product;

    }

    /**
     * Añade un producto de catering al sistema.
     *
     * @param id               Identificador único del producto.
     * @param name             Nombre del producto (no puede ser nulo, vacío o superar {@value #MAX_NAME_LENGTH} caracteres).
     * @param price            Precio del producto (debe ser mayor que 0).
     * @param expirationDate   Fecha de caducidad del producto (no puede ser nula).
     * @param maxParticipants   Número máximo de participantes (debe ser mayor o igual a 1).
     * @return La instancia de {@link AbstractProductDTO} creada.
     * @throws IllegalArgumentException Si el precio es negativo, el nombre no es válido, la fecha de caducidad es nula, maxParticipants es menor que 1 o el producto ya existe.
     * @throws RuntimeException Si se ha alcanzado el número máximo de productos permitidos.
     */
    public AbstractProductDTO addFoodProduct(int id, String name, double price, LocalDateTime expirationDate, int maxParticipants){
        if (!isPriceValid(price)) throw new IllegalArgumentException("Product price " + price + " cannot be negative.");
        if (!isNameValid(name)) throw new IllegalArgumentException("Product name \"" + price + "\" is invalid.");
        if (isProductListFull()) throw new RuntimeException("Product cannot be added, there are " + MAX_PRODUCTS + " or more products.");
        if (maxParticipants < 1) throw new IllegalArgumentException("Max participants " + maxParticipants + " must be >= 1.");
        if (expirationDate == null) throw new IllegalArgumentException("Invalid expiration date.");
        if (existId(id)) throw new IllegalArgumentException("Product id " + id + " already exists.");


        AbstractProductDTO food = new CateringDTO(id, name,price, maxParticipants, expirationDate);
        add(food, id);
        return  food;
    }

    /**
     * Añade un producto de reunión al sistema.
     *
     * @param id               Identificador único del producto.
     * @param name             Nombre del producto (no puede ser nulo, vacío o superar {@value #MAX_NAME_LENGTH} caracteres).
     * @param pricePerPerson   Precio por persona (debe ser mayor que 0).
     * @param expirationDate   Fecha de caducidad del producto (no puede ser nula).
     * @param maxParticipants   Número máximo de participantes (debe ser mayor o igual a 1).
     * @return La instancia de {@link AbstractProductDTO} creada.
     * @throws IllegalArgumentException Si el precio es negativo, el nombre no es válido, la fecha de caducidad es nula, maxParticipants es menor que 1 o el producto ya existe.
     * @throws RuntimeException Si se ha alcanzado el número máximo de productos permitidos.
     */
    public AbstractProductDTO addMeetingProduct(int id, String name, double pricePerPerson, LocalDateTime expirationDate, int maxParticipants){
        if (!isPriceValid(pricePerPerson)) throw new IllegalArgumentException("Product price " + pricePerPerson + " cannot be negative.");
        if (!isNameValid(name)) throw new IllegalArgumentException("Product name \"" + name + "\" is invalid.");
        if (isProductListFull()) throw new RuntimeException("Product cannot be added, there are " + MAX_PRODUCTS + " or more products.");
        if (maxParticipants < 1) throw new IllegalArgumentException("Max participants " + maxParticipants + " must be >= 1.");
        if (expirationDate == null) throw new IllegalArgumentException("Invalid expiration date.");
        if (existId(id)) throw new IllegalArgumentException("Product id " + id + " already exists.");


        AbstractProductDTO meeting = new MeetingDTO(id, name, pricePerPerson, maxParticipants, expirationDate);
        add(meeting, id);
        return meeting;
    }

    /**
     * Valida el nombre de un producto.
     *
     * @param name Nombre del producto a validar.
     * @return {@code true} si el nombre es válido (no nulo, no vacío y no supera {@value #MAX_NAME_LENGTH} caracteres).
     */
    public boolean isNameValid(String name) {
        return name != null && !name.isBlank() && name.length() < MAX_NAME_LENGTH;
    }

    /**
     * Valida el precio de un producto.
     *
     * @param price Precio del producto a validar.
     * @return {@code true} si el precio es mayor que 0.
     */
    public boolean isPriceValid(double price) {
        return price > 0;
    }

    /**
     * Comprueba si se ha alcanzado el número máximo de productos permitidos.
     *
     * @return {@code true} si el número de productos es mayor o igual a {@value #MAX_PRODUCTS}.
     */
    public boolean isProductListFull() {
        return getSize() >= ProductService.MAX_PRODUCTS;
    }


}
