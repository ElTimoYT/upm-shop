package es.upm.iwsim22_01.factory;

import es.upm.iwsim22_01.manager.ProductManager;
import es.upm.iwsim22_01.models.Category;
import es.upm.iwsim22_01.models.Product;

public class ProductFactory {
    private static final int MAX_NAME_LENGTH = 100;

    private ProductManager productManager;

    public ProductFactory(ProductManager productManager) {
        this.productManager = productManager;
    }

    public Product createProduct(int id, String name, Category category, double price) {
        if (productManager.existId(id)) throw new IllegalArgumentException("Product with id " + id + " already exists.");
        if (!isPriceValid(price)) throw new IllegalArgumentException("Product price " + price + " cannot be negative.");
        if (!isNameValid(name)) throw new IllegalArgumentException("Product name \"" + price + "\" is invalid.");

        return new Product(id, name, category, price);
    }

    public boolean isNameValid(String name) {
        return name != null && !name.isBlank() && name.length() < MAX_NAME_LENGTH;
    }

    public boolean isPriceValid(double price) {
        return price > 0;
    }
}
