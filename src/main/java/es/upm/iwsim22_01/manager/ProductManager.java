package es.upm.iwsim22_01.manager;

import es.upm.iwsim22_01.models.*;

import java.time.LocalDate;

public class ProductManager extends AbstractManager<Product, Integer> {
    private final static int MAX_PRODUCTS = 200, MAX_NAME_LENGTH = 100;

    public Product addProduct(int id, String name, Category category, double price) {
        if (!isPriceValid(price)) throw new IllegalArgumentException("Product price " + price + " cannot be negative.");
        if (!isNameValid(name)) throw new IllegalArgumentException("Product name \"" + price + "\" is invalid.");
        if (isProductListFull()) throw new RuntimeException("Product cannot be added, there are " + MAX_PRODUCTS + " or more products.");

        Product product = new Product(id, name, category, price);
        add(product, id);

        return product;
    }

    public Product addCustomizableProduct(int id, String name, Category category, double price, int maxText){
        if (!isPriceValid(price)) throw new IllegalArgumentException("Product price " + price + " cannot be negative.");
        if (!isNameValid(name)) throw new IllegalArgumentException("Product name \"" + price + "\" is invalid.");
        if (isProductListFull()) throw new RuntimeException("Product cannot be added, there are " + MAX_PRODUCTS + " or more products.");
        if (maxText < 1) throw new IllegalArgumentException("Max pers " + maxText + " cannot be less than 1.");
        if (existId(id)) throw new IllegalArgumentException("Product id " + id + " already exists(cannot convert basic to customizable).");

        Product product = new PersonalizableProduct(id, name, category, price, maxText);
        add(product, id);

        return  product;

    }

    public Product addFoodProduct(int id, String name, double price, LocalDate expirationDate, int maxParticipants){
        if (!isPriceValid(price)) throw new IllegalArgumentException("Product price " + price + " cannot be negative.");
        if (!isNameValid(name)) throw new IllegalArgumentException("Product name \"" + price + "\" is invalid.");
        if (isProductListFull()) throw new RuntimeException("Product cannot be added, there are " + MAX_PRODUCTS + " or more products.");
        if (maxParticipants < 1) throw new IllegalArgumentException("Max participants " + maxParticipants + " must be >= 1.");
        if (expirationDate == null) throw new IllegalArgumentException("Invalid expiration date.");
        if (existId(id)) throw new IllegalArgumentException("Product id " + id + " already exists.");
        if (!isFoodDateAllowed(expirationDate)) throw new IllegalStateException("Temporal constraint not satisfied for Catering.");


        Product food = new Catering(id, name,price, maxParticipants, expirationDate);
        add(food, id);
        return  food;
    }

    public Product addMeetingProduct(int id, String name, Category category, double pricePerPerson, LocalDate expirationDate, int maxParticipants){
        if (!isPriceValid(pricePerPerson)) throw new IllegalArgumentException("Product price " + pricePerPerson + " cannot be negative.");
        if (!isNameValid(name)) throw new IllegalArgumentException("Product name \"" + name + "\" is invalid.");
        if (isProductListFull()) throw new RuntimeException("Product cannot be added, there are " + MAX_PRODUCTS + " or more products.");
        if (maxParticipants < 1) throw new IllegalArgumentException("Max participants " + maxParticipants + " must be >= 1.");
        if (expirationDate == null) throw new IllegalArgumentException("Invalid expiration date.");
        if (existId(id)) throw new IllegalArgumentException("Product id " + id + " already exists.");
        if (!isMeetingDateAllowed(expirationDate)) throw new IllegalStateException("Temporal constraint not satisfied for Meeting.");

        Product meeting = new Meetings(id, name, pricePerPerson, maxParticipants, expirationDate);
        add(meeting, id);
        return meeting;
    }

    public boolean isNameValid(String name) {
        return name != null && !name.isBlank() && name.length() < MAX_NAME_LENGTH;
    }

    public boolean isPriceValid(double price) {
        return price > 0;
    }

    public boolean isProductListFull() {
        return getSize() >= ProductManager.MAX_PRODUCTS;
    }

    public boolean isFoodDateAllowed(LocalDate expiration) {
        return !expiration.isBefore(LocalDate.now().plusDays(3));
    }
    public boolean isMeetingDateAllowed(LocalDate expiration) {
        return !expiration.isBefore(LocalDate.now());
    }
}
