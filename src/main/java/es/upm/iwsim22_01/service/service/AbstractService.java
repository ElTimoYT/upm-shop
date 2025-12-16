package es.upm.iwsim22_01.service.service;

import java.util.*;

/**
 * Clase base genérica para gestionar colecciones de elementos identificados por una clave.
 * Proporciona operaciones comunes como añadir, obtener, eliminar y consultar elementos.
 *
 * @param <T> tipo de los elementos gestionados
 * @param <K> tipo de la clave que identifica cada elemento
 */
public abstract class AbstractService<T, K> {
     private Map<K, T> elements = new HashMap<>();

    /**
     * Añade un elemento asociado a una clave.
     * Si la clave ya existe o si el elemento es nulo, se lanza una excepción.
     *
     * @param element elemento a añadir
     * @param key clave asociada al elemento
     * @throws IllegalArgumentException si la clave ya existe o el elemento es nulo
     */
     protected void add(T element, K key) {
        if (existId(key)) throw new IllegalArgumentException("Element with ID " + key + "already exists.");
        if (element == null) throw new IllegalArgumentException("Element cannot be null.");

        elements.put(key, element);
    }

    /**
     * Elimina un elemento identificado por su clave.
     * Si no existe, devuelve null.
     *
     * @param id clave del elemento a eliminar
     * @return elemento eliminado, o null si no existía
     */
    public T remove(K id) {
        return elements.remove(id);
    }

    /**
     * Obtiene un elemento por su clave.
     *
     * @param id clave del elemento a buscar
     * @return elemento asociado a la clave
     * @throws IllegalArgumentException si no existe ningún elemento asociado a esa clave
     */
    public T get(K id) {
         if (!existId(id)) throw new IllegalArgumentException("Element with ID " + id + "doesn`t exists.");

         return elements.get(id);
    }

    /**
     * Obtiene todos los elementos almacenados.
     * Se devuelve una nueva lista para evitar modificaciones externas.
     *
     * @return lista de todos los elementos gestionados
     */
    public List<T> getAll() {
        return new ArrayList<>(elements.values());
    }

    /**
     * Comprueba si existe un elemento para una clave dada.
     *
     * @param id clave a comprobar
     * @return true si existe un elemento con esa clave, false en caso contrario
     */
    public boolean existId(K id) {
        return elements.containsKey(id);
    }

    /**
     * Devuelve el número total de elementos almacenados.
     *
     * @return número de elementos
     */
    protected int getSize() {
        return elements.size();
    }

}
