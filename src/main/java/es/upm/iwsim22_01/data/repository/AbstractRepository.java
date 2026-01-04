package es.upm.iwsim22_01.data.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import es.upm.iwsim22_01.data.repository.adapters.LocalDateTimeAdapter;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractRepository<T, K> implements Repository<T, K> {
    protected static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    protected Map<K, T> cache;

    protected abstract String getFilePath();
    protected abstract K getId(T element);
    protected abstract TypeToken<List<T>> getTypeToken();

    protected File getFile() throws IOException {
        File file = new File(getFilePath());
        file.getParentFile().mkdirs();

        if (!file.exists()) {
            file.createNewFile();
            try (Writer writer = new FileWriter(file)) {
                GSON.toJson(new ArrayList<>(), writer);
            }
        }
        return file;
    }

    protected void loadCacheIfNeeded() {
        if (cache != null) return;

        try (Reader reader = new FileReader(getFile())) {
            List<T> elements = GSON.fromJson(reader, getTypeToken().getType());
            cache = new HashMap<>();

            if (elements != null) {
                for (T element : elements) {
                    cache.put(getId(element), element);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading repository", e);
        }
    }

    protected void persist() {
        try (Writer writer = new FileWriter(getFile())) {
            GSON.toJson(cache.values(), writer);
        } catch (IOException e) {
            throw new RuntimeException("Error saving repository", e);
        }
    }

    @Override
    public T create(T element) {
        if (element == null) throw new IllegalArgumentException("Element cannot be null.");

        loadCacheIfNeeded();

        K id = getId(element);
        if (id == null) throw new IllegalArgumentException("Id cannot be null.");
        if (cache.containsKey(id)) throw new IllegalArgumentException("Id " + id + " must be unique");

        cache.put(id, element);
        persist();

        return element;
    }

    @Override
    public T update(T element) {
        if (element == null) throw new IllegalArgumentException("Element cannot be null.");

        loadCacheIfNeeded();

        K id = getId(element);
        if (!cache.containsKey(id)) {
            throw new IllegalArgumentException("Element with id " + id + " not found");
        }

        cache.put(id, element);
        persist();

        return element;
    }

    @Override
    public T get(K id) {
        if (id == null) throw new IllegalArgumentException("Element cannot be null.");

        loadCacheIfNeeded();

        return cache.get(id);
    }

    @Override
    public T remove(K id) {
        if (id == null) throw new IllegalArgumentException("Id cannot be null.");

        loadCacheIfNeeded();

        T removed = cache.remove(id);

        persist();

        return removed;
    }

    @Override
    public List<T> getAll() {
        loadCacheIfNeeded();
        return new ArrayList<>(cache.values());
    }

    @Override
    public boolean existsId(K id) {
        if (id == null) throw new IllegalArgumentException("Id cannot be null.");

        loadCacheIfNeeded();
        return cache.containsKey(id);
    }

    @Override
    public int getSize() {
        loadCacheIfNeeded();
        return cache.size();
    }
}