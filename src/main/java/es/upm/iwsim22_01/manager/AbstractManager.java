package es.upm.iwsim22_01.manager;

import java.util.*;

public abstract class AbstractManager<T, K> {
    private Map<K, T> elements = new HashMap<>();

    public abstract void add(T element);

    protected void add(T element, K key) {
        if (element == null || existId(key)) return;

        elements.put(key, element);
    }

    public void remove(K id) {
        elements.remove(id);
    }

    public Optional<T> get(K id) {
        return Optional.ofNullable(elements.get(id));
    }

    public Collection<T> getAll() {
        return elements.values();
    }

    public boolean existId(K id) {
        return elements.containsKey(id);
    }

    protected int getSize() {
        return elements.size();
    }

}
