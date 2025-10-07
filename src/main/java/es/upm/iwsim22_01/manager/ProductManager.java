package es.upm.iwsim22_01.manager;

import es.upm.iwsim22_01.models.Product;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ProductManager {
    private static ProductManager PRODUCT_MANAGER;

    private final static int MAX_PRODUCTS = 200;

    private Set<Product> products = new HashSet<>();

    public static ProductManager getProductManager() {
        if (ProductManager.PRODUCT_MANAGER == null) {
            ProductManager.PRODUCT_MANAGER = new ProductManager();
        }

        return ProductManager.PRODUCT_MANAGER;
    }

    public boolean addProduct(Product product) {
        if (product == null || products.size() >= ProductManager.MAX_PRODUCTS) {
            return false;
        }

        return products.add(product);
    }

    public boolean removeProduct(int id) {
        return products.removeIf(p -> p.getId() == id);
    }

    public Optional<Product> getProduct(int id) {
        return products.stream().filter(p -> p.getId() == id).findFirst();
    }

    public Set<Product> getProducts() {
        return new HashSet<>(this.products);
    }

    public boolean existId(int id) {
        return products.stream().anyMatch(p -> p.getId() == id);
    }

}
