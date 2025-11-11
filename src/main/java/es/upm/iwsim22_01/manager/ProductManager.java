package es.upm.iwsim22_01.manager;

import es.upm.iwsim22_01.models.Category;
import es.upm.iwsim22_01.models.Product;

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

    public Product addCustomizableProduct(int id, String name, Category category, double price, int maxPers){
        if (!isPriceValid(price)) throw new IllegalArgumentException("Product price " + price + " cannot be negative.");
        if (!isNameValid(name)) throw new IllegalArgumentException("Product name \"" + price + "\" is invalid.");
        if (isProductListFull()) throw new RuntimeException("Product cannot be added, there are " + MAX_PRODUCTS + " or more products.");
        if (maxPers < 1) throw new IllegalArgumentException("Max pers " + maxPers + " cannot be less than 1.");
        if (existId(id)) throw new IllegalArgumentException("Product id " + id + " already exists(cannot convert basic to customizable).");

        Product product = new Product(id, name, category, price, maxPers);
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
}
