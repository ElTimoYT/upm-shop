package es.upm.iwsim22_01.data.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import es.upm.iwsim22_01.data.models.*;
import es.upm.iwsim22_01.data.repository.adapters.LocalDateTimeAdapter;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepository implements Repository<Product, Integer> {
    private static final String FILE = "data/products/products.json";
    private static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
        .create();

    private File getFile() throws IOException {
        File file = new File(FILE);

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if (!file.exists()) {
            file.createNewFile();

            try (Writer writer = new FileWriter(file)) {
                GSON.toJson(new ArrayList<>(), writer);
            }
        }

        System.out.println(file);
        return file;
    }

    private List<Product> readFile() throws IOException {
        try (Reader reader = new FileReader(getFile())) {
            Type type = new TypeToken<List<Product>>() {
            }.getType();

            List<Product> products = GSON.fromJson(reader, type);

            return products != null ? products : new ArrayList<>();
        }
    }

    private void updateFile(List<Product> products) throws IOException {
        try (Writer writer = new FileWriter(getFile())) {
            GSON.toJson(products, writer);
        }
    }

    public Product createUnitProduct(int id, String name, String category, double price) {
        return create(Product.createUnit(id, name, category, price));
    }

    public Product createPersonalizable(int id, String name, String category, double price, int lines) {
        return create(Product.createPersonalizable(id, name, category, price, lines));
    }

    public Product createMeeting(int id, String name, double price, int maxParticipant, LocalDateTime expirationDate, int participantsAmount) {
        return create(Product.createMeeting(id, name, price, maxParticipant, expirationDate, participantsAmount));
    }

    public Product createCatering(int id, String name, double price, int maxParticipant, LocalDateTime expirationDate, int participantsAmount) {
        return create(Product.createCatering(id, name, price, maxParticipant, expirationDate, participantsAmount));
    }

    @Override
    public Product create(Product product) {
        try {
            List<Product> products = readFile();

            if (products.stream().anyMatch(p -> p.getId() == product.getId())) throw new IllegalArgumentException("Id " + product.getId() + " must be unique");

            products.add(product);
            updateFile(products);

            return product;
        } catch (IOException exception) {
            throw new RuntimeException("Error writing/reading the file");
        }
    }

    @Override
    public Product update(Product element) {
        try {
            List<Product> products = readFile();

            Optional<Product> unitProduct = products.stream()
                    .filter(p -> p.getId() == element.getId())
                    .findFirst();

            if (unitProduct.isEmpty()) throw new IllegalArgumentException("Product with id " + element.getId() + " not found");
            products.remove(unitProduct.get());
            products.add(element);

            updateFile(products);

            return element;
        } catch (IOException e) {
            throw new RuntimeException("Error writing/reading the file");
        }
    }

    @Override
    public Product get(Integer id) {
        try {
            List<Product> products = readFile();

            Optional<Product> product =  products.stream()
                    .filter(p -> p.getId() == id)
                    .findFirst();

            if (product.isEmpty()) throw new IllegalArgumentException("Product with id " + id + " not found");

            return product.get();
        } catch (IOException exception) {
            throw new RuntimeException("Error writing/reading the file");
        }
    }

    @Override
    public Product remove(Integer id) {
        try {
            List<Product> products = readFile();
            
            Product productToRemove = get(id);
            products.remove(productToRemove);

            updateFile(products);

            return productToRemove;
        } catch (IOException exception) {
            throw new RuntimeException("Error writing/reading the file");
        }
    }

    @Override
    public List<Product> getAll() {
        try {
            return readFile();
        } catch (IOException exception) {
            throw new RuntimeException("Error writing/reading the file");
        }
    }

    @Override
    public boolean existsId(Integer id) {
        return getAll().stream().anyMatch(product -> product.getId() == id);
    }


    @Override
    public int getSize() {
        try {
            List<Product> products = readFile();

            return products.size();
        } catch (IOException exception) {
            throw new RuntimeException("Error writing/reading the file");
        }
    }
}
