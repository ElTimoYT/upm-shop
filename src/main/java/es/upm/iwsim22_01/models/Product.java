package es.upm.iwsim22_01.models;

public class Product {
    private final int id;
    private String name;
    private Category category;
    private double price;

    public Product(int id, String name, Category category, double price) {
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
        this.price = price;
    }

    public enum Category {
        MERCH,
        PAPELERIA,
        ROPA,
        LIBRO;
    }
}
