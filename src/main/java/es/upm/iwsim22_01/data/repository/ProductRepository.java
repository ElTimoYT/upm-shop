package es.upm.iwsim22_01.data.repository;

import com.google.gson.reflect.TypeToken;
import es.upm.iwsim22_01.data.models.Product;

import java.io.*;
import java.util.*;

public class ProductRepository extends AbstractRepository<Product, String> {

    private int autoincrementId = 1;

    @Override
    protected String getFilePath() {
        return "data/products/products.json";
    }

    @Override
    protected String getId(Product product) {
        return product.getId();
    }

    @Override
    protected TypeToken<List<Product>> getTypeToken() {
        return new TypeToken<>() {
        };
    }

    @Override
    protected void loadCacheIfNeeded() {
        if (cache != null) return;

        cache = new HashMap<>();

        try (Reader reader = new FileReader(getFile())) {
            Map<String, Object> root = GSON.fromJson(
                    reader,
                    new TypeToken<Map<String, Object>>() {}.getType()
            );

            if (root == null) return;

            // autoincrementId
            if (root.containsKey("autoincrementId")) {
                autoincrementId = ((Double) root.get("autoincrementId")).intValue();
            }

            // products
            List<?> products = (List<?>) root.get("products");
            if (products != null) {
                for (Object obj : products) {
                    Product product = GSON.fromJson(
                            GSON.toJson(obj),
                            Product.class
                    );
                    cache.put(product.getId(), product);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Error loading ProductRepository", e);
        }
    }

    @Override
    protected void persist() {
        Map<String, Object> root = new HashMap<>();
        root.put("autoincrementId", autoincrementId);
        root.put("products", cache.values());

        try (Writer writer = new FileWriter(getFile())) {
            GSON.toJson(root, writer);
        } catch (IOException e) {
            throw new RuntimeException("Error saving ProductRepository", e);
        }
    }

    public int getAutoincrementId() {
        loadCacheIfNeeded();
        return autoincrementId;
    }

    public int nextAutoincrementId() {
        loadCacheIfNeeded();
        return autoincrementId++;
    }
}
