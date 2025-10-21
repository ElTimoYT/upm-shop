package es.upm.iwsim22_01.models;

import java.util.Objects;
import java.util.OptionalDouble;
import java.util.OptionalInt;

import es.upm.iwsim22_01.manager.ProductManager;

public class Product {
    private static final int MAX_NAME_LENGTH = 100;

    private final int id;
    private String name;
    private Category category;
    private double price;

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

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
       if (!checkName(name)) {
           throw new IllegalArgumentException("Invalid name");
       }

        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (!checkPrice(price)) {
            throw new IllegalArgumentException("Invalid price");
        }

        this.price = price;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Product product) {
            return product.getId() == this.getId();
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getId());
    }

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

    public static boolean checkId(int id) {
        return !ProductManager.getProductManager().existId(id);
    }

    public static boolean checkId(OptionalInt id) {
        return id.isPresent() && checkId(id.getAsInt());
    }

    public static boolean checkName(String name) {
        return name != null && !name.isBlank() && name.length() < Product.MAX_NAME_LENGTH;
    }

    public static boolean checkPrice(double price) {
        return price > 0;
    }

    public static boolean checkPrice(OptionalDouble price) {
        return price.isPresent() && checkPrice(price.getAsDouble());
    }
}
