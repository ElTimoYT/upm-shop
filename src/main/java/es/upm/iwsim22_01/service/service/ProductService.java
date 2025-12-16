package es.upm.iwsim22_01.service.service;

import es.upm.iwsim22_01.data.models.*;
import es.upm.iwsim22_01.data.repository.ProductRepository;
import es.upm.iwsim22_01.service.dto.product.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Gestor de productos, encargado de la creación, validación y registro de instancias de {@link AbstractProductDTO}.
 * Permite añadir diferentes tipos de productos al sistema, validando sus parámetros y restricciones.
 */
public class ProductService {
    private final static int MAX_PRODUCTS = 200, MAX_NAME_LENGTH = 100;

    private final ProductRepository repository = new ProductRepository();

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

        Product unitProduct = repository.createUnitProduct(id, name, category.toString(), price);
        return toDTO(unitProduct);
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
    public AbstractProductDTO addPersonalizableProduct(int id, String name, CategoryDTO category, double price, int maxText){
        if (!isPriceValid(price)) throw new IllegalArgumentException("Product price " + price + " cannot be negative.");
        if (!isNameValid(name)) throw new IllegalArgumentException("Product name \"" + price + "\" is invalid.");
        if (isProductListFull()) throw new RuntimeException("Product cannot be added, there are " + MAX_PRODUCTS + " or more products.");
        if (maxText < 1) throw new IllegalArgumentException("Max pers " + maxText + " cannot be less than 1.");

        Product personalizableProduct = repository.createPersonalizable(id, name, category.toString(), price, maxText);
        return this.toDTO(personalizableProduct);
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

        Product catering = repository.createCatering(id, name, price, maxParticipants, expirationDate, 0);
        return toDTO(catering);
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


        Product meeting = repository.createMeeting(id, name, pricePerPerson, maxParticipants, expirationDate, 0);
        return toDTO(meeting);
    }

    public AbstractProductDTO remove(int id) {
        return toDTO(repository.remove(id));
    }

    public AbstractProductDTO get(int id) {
        Product product = repository.get(id);

        return toDTO(product);
    }

    public List<AbstractProductDTO> getAll() {
        return repository.getAll().map(this::toDTO).toList();
    }

    public boolean existId(int id) {
        return repository.existsId(id);
    }

    protected int getSize() {
        return repository.getSize();
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
        return repository.getSize() >= ProductService.MAX_PRODUCTS;
    }

    public AbstractProductDTO toDTO(Product product) {
        switch (product.getType()) {
            case UNIT_PRODUCT -> {
                return toUnitDTO(product);
            }
            case PERSONALIZABLE_PRODUCT -> {
                return toPersonalizableDTO(product);
            }
            case CATERING -> {
                return toCateringDTO(product);
            }
            case MEETING -> {
                return toMeetingDTO(product);
            }
            case null, default -> {
                return null;
            }
        }
    }

    public UnitProductDTO toUnitDTO(Product unitProduct) {
        return new UnitProductDTO(unitProduct.getId(), unitProduct.getName(), CategoryDTO.valueOf(unitProduct.getCategory()), unitProduct.getPrice());
    }

    public PersonalizableProductDTO toPersonalizableDTO(Product personalizableProduct) {
        return new PersonalizableProductDTO(personalizableProduct.getId(), personalizableProduct.getName(), CategoryDTO.valueOf(personalizableProduct.getCategory()), personalizableProduct.getPrice(), personalizableProduct.getLines());
    }

    public CateringDTO toCateringDTO(Product catering) {
        return new CateringDTO(catering.getId(), catering.getName(), catering.getPrice(), catering.getMaxParticipant(), catering.getExpirationDate(), catering.getParticipantsAmount());
    }

    public MeetingDTO toMeetingDTO(Product meeting) {
        return new MeetingDTO(meeting.getId(), meeting.getName(), meeting.getPrice(), meeting.getMaxParticipant(), meeting.getExpirationDate(), meeting.getParticipantsAmount());
    }
}
