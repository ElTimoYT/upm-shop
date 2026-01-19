package es.upm.iwsim22_01.data.repository;

import java.util.List;


/**
 * Interfaz que define las operaciones básicas de un repositorio genérico.
 *
 * Proporciona métodos para crear, consultar, actualizar y eliminar elementos,
 * así como utilidades para comprobar la existencia y el tamaño del repositorio.
 *
 * @param <T> tipo de los elementos gestionados
 * @param <K> tipo del identificador único de los elementos
 */
public interface Repository<T, K> {
    /**
     * Crea un nuevo elemento en el repositorio.
     *
     * @param element elemento a almacenar
     * @return elemento creado
     * @throws IllegalArgumentException si el elemento es nulo o el identificador ya existe
     */
    T create(T element);


    /**
     * Obtiene un elemento a partir de su identificador.
     *
     * @param id identificador del elemento
     * @return elemento asociado al identificador o null si no existe
     * @throws IllegalArgumentException si el identificador es nulo
     */
    T get(K id);


    /**
     * Devuelve una lista con todos los elementos del repositorio.
     *
     * @return lista de elementos almacenados
     */
    List<T> getAll();

    /**
     * Actualiza un elemento existente en el repositorio.
     *
     * @param element elemento con los datos actualizados
     * @return elemento actualizado
     *
     */
    T update(T element);

    /**
     * Elimina un elemento del repositorio.
     *
     * @param id identificador del elemento a eliminar
     * @return elemento eliminado o null si no existía
     */
    T remove(K id);

    /**
     * Comprueba si existe un elemento con el identificador indicado.
     *
     * @param id identificador a comprobar
     * @return true si existe un elemento con ese identificador
     */
    boolean existsId(K id);

    /**
     * Devuelve el número total de elementos almacenados.
     *
     * @return tamaño del repositorio
     */
    int getSize();
}
