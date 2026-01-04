package es.upm.iwsim22_01.data.repository;

import com.google.gson.reflect.TypeToken;
import es.upm.iwsim22_01.data.models.Product;

import java.util.*;

public class ProductRepository extends AbstractRepository<Product, Integer> {
    @Override
    protected String getFilePath() {
        return "data/products/products.json";
    }

    @Override
    protected Integer getId(Product product) {
        return product.getId();
    }

    @Override
    protected TypeToken<List<Product>> getTypeToken() {
        return new TypeToken<>() {
        };
    }
}
