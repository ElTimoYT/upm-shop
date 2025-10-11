package es.upm.iwsim22_01.manager;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import es.upm.iwsim22_01.models.Product;

/**
 * Gestor singleton que administra la colección de productos del sistema.
 * <p>
 * Esta clase implementa el patrón Singleton para asegurar que solo existe
 * una instancia del gestor de productos en toda la aplicación.
 * Proporciona operaciones CRUD para gestionar los productos.
 * </p>
 */
public class ProductManager {
    private static ProductManager productManager;
    private final static int MAX_PRODUCTS = 200;

    private Set<Product> products = new HashSet<>();

    private ProductManager() {}

    /**
     * Obtiene la instancia única del ProductManager.
     * Si no existe, la crea (patrón Singleton).
     * 
     * @return la instancia única del ProductManager
     */
    public static ProductManager getProductManager() {
        if (ProductManager.productManager == null) {
            ProductManager.productManager = new ProductManager();
        }

        return ProductManager.productManager;
    }

    /**
     * Añade un producto al gestor.
     * 
     * @param product producto a añadir
     * @return true si el producto se añadió correctamente, false en caso contrario
     */
    public boolean addProduct(Product product) {
        if (product == null || products.size() >= ProductManager.MAX_PRODUCTS) {
            return false;
        }

        return products.add(product);
    }

    /**
     * Elimina un producto del gestor por su identificador.
     * 
     * @param id identificador del producto a eliminar
     * @return true si se eliminó el producto, false en caso contrario
     */
    public boolean removeProduct(int id) {
        return products.removeIf(p -> p.getId() == id);
    }

    /**
     * Busca un producto por su identificador.
     * 
     * @param id identificador del producto a buscar
     * @return Optional que contiene el producto si se encuentra, vacío en caso contrario
     */
    public Optional<Product> getProduct(int id) {
        return products.stream().filter(p -> p.getId() == id).findFirst();
    }

    /**
     * Obtiene una copia del conjunto de todos los productos.
     * 
     * @return conjunto con todos los productos (copia defensiva)
     */
    public Set<Product> getProducts() {
        return new HashSet<>(this.products);
    }

    /**
     * Verifica si existe un producto con el identificador especificado.
     * 
     * @param id identificador a verificar
     * @return true si existe un producto con ese identificador, false en caso contrario
     */
    public boolean existId(int id) {
        return products.stream().anyMatch(p -> p.getId() == id);
    }

}
