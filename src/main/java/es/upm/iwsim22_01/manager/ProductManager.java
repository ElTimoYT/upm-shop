package es.upm.iwsim22_01.manager;

import es.upm.iwsim22_01.models.Product;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ProductManager {
    private final static int MAX_PRODUCTS = 200;

    private Set<Product> products = new HashSet<>();

    public boolean addProduct(Product product) {
        if (product == null || products.size() >= ProductManager.MAX_PRODUCTS) {
            return false;
        }

        return products.add(product);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

    public Optional<Product> getProduct(int id) {
        return products.stream().filter(p -> p.getId() == id).findFirst();
    }

    public Set<Product> getProducts() {
        return new HashSet<>(this.products);
    }

}
