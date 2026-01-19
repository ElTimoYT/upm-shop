package es.upm.iwsim22_01.service.service;

import es.upm.iwsim22_01.data.repository.Repository;

import java.util.*;

/**
 * Clase base genérica para gestionar servicios que operan sobre repositorios y transforman modelos en DTOs.
 *
 * @param <M> tipo del modelo de dominio
 * @param <D> tipo del DTO expuesto por el servicio
 * @param <K> tipo de la clave identificadora del modelo
 */
public abstract class AbstractService<M, D, K> {
    protected final Repository<M, K> repository;

    /**
     * Crea un servicio asociado a un repositorio concreto.
     *
     * @param repository repositorio utilizado por el servicio
     */
    protected AbstractService(Repository<M, K> repository) {
        this.repository = repository;
    }

    /**
     * Convierte un modelo de dominio en su correspondiente DTO.
     *
     * @param model modelo a convertir
     * @return DTO resultante
     */
    protected abstract D toDto(M model);

    /**
     * Convierte un DTO en su correspondiente modelo de dominio.
     *
     * @param dto DTO a convertir
     * @return modelo resultante
     */
    protected abstract M toModel(D dto);

    /**
     * Añade un nuevo elemento al repositorio a partir de un DTO.
     *
     * @param dto DTO a añadir
     * @return DTO del elemento añadido
     */
    protected D add(D dto) {
        if (dto == null) throw new IllegalArgumentException("DTO cannot be null");

        M saved = repository.create(toModel(dto));
        return toDto(saved);
    }

    /**
     * Actualiza un elemento existente en el repositorio a partir de un DTO.
     *
     * @param dto DTO con los datos actualizados
     * @return DTO del elemento actualizado
     */
    public D update(D dto) {
        if (dto == null) throw new IllegalArgumentException("DTO cannot be null");

        M updated = repository.update(toModel(dto));
        return toDto(updated);
    }

    /**
     * Obtiene un elemento a partir de su identificador.
     *
     * @param id identificador del elemento
     * @return DTO correspondiente al elemento
     */
    public D get(K id) {
         return toDto(repository.get(id));
    }

    /**
     * Elimina un elemento del repositorio a partir de su identificador.
     *
     * @param id identificador del elemento
     * @return DTO del elemento eliminado
     */
    public D remove(K id) {
        return toDto(repository.remove(id));
    }

    /**
     * Devuelve una lista con todos los elementos del repositorio convertidos a DTOs.
     *
     * @return lista de DTOs
     */
    public List<D> getAll() {
        return new ArrayList<>(
                repository.getAll()
                        .stream()
                        .map(this::toDto)
                        .toList()
        );
    }

    /**
     * Indica si existe un elemento con el identificador indicado.
     *
     * @param id identificador a comprobar
     * @return true si el identificador existe
     */
    public boolean existsId(K id) {
        return repository.existsId(id);
    }

    /**
     * Devuelve el número total de elementos gestionados por el servicio.
     *
     * @return número de elementos
     */
    public int getSize() {
        return repository.getSize();
    }

}
