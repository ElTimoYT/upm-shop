package es.upm.iwsim22_01.manager;

import java.util.*;

public abstract class AbstractManager<T, K> {
    private Map<K, T> elements = new HashMap<>();

     protected void add(T element, K key) {
        if (existId(key)) throw new IllegalArgumentException("Element with ID " + key + "already exists.");
        if (element == null) throw new IllegalArgumentException("Element cannot be null.");

        elements.put(key, element);
    }

    public void remove(K id) {
        elements.remove(id);
    }

    public Optional<T> get(K id) {
        return Optional.ofNullable(elements.get(id));
    }

    public List<T> getAll() {
        return new ArrayList<>(elements.values());
    }

    public boolean existId(K id) {
        return elements.containsKey(id);
    }

    protected int getSize() {
        return elements.size();
    }

}
