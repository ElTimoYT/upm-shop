package es.upm.iwsim22_01.models;

import java.util.Objects;


public abstract class Product {
    private final int id;
    private String name;
    private double price;

    public Product(int id, String name, double price) {
        this.id = id;
        this.name = name;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object obj) {
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
                ",price:" + price +
                '}';
    }
}
