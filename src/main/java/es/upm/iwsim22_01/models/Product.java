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

    

    public enum Category {
        MERCH,
        PAPELERIA,
        ROPA,
        LIBRO;
    }
}
