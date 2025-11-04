package es.upm.iwsim22_01.manager;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import es.upm.iwsim22_01.models.Product;

public class ProductManager extends AbstractManager<Product, Integer> {
    private final static int MAX_PRODUCTS = 200;

    @Override
    public void add(Product product) {
        if (getSize() >= ProductManager.MAX_PRODUCTS) return;

        add(product, product.getId());
    }
}
