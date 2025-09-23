package es.upm.iwsim22_01.models;

import java.util.Objects;

public class Product {
    private static final int MAX_NAME_LENGTH = 100;

    private final int id;
    private String name;
    private Category category;
    private double price;

    private Product(int id, Category category) {
        this.id = id;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean setName(String name) {
        if (name == null || name.isEmpty() || name.isBlank() || name.length() >= Product.MAX_NAME_LENGTH) {
            return false;
        }

        this.name = name;
        return true;
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

    public boolean setPrice(double price) {
        if (price <= 0) {
            return false;
        }

        this.price = price;
        return true;
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
                "class: " + this.getClass() +
                "id:" + id +
                ",name:'" + name + '\'' +
                ",category:" + category +
                ",price:" + price +
                '}';
    }

    public static Product createNewProduct(int id, String name, Category category, double price) {
        if (id < 0) {
            return null;
        }

        Product newProduct = new Product(id, category);

        if (newProduct.setName(name) && newProduct.setPrice(price)) {
            return newProduct;
        }

        return null;
    }

}
