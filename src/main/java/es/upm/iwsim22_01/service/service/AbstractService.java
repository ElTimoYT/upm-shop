package es.upm.iwsim22_01.service.service;

import es.upm.iwsim22_01.data.repository.Repository;

import java.util.*;

/**
 * Clase base genérica para gestionar colecciones de elementos identificados por una clave.
 * Proporciona operaciones comunes como añadir, obtener, eliminar y consultar elementos.
 *
 * @param <M> tipo de los modelos de los elementos gestionados
 * @param <D> tipo de los DTOs de los elementos gestionados
 * @param <K> tipo de la clave que identifica cada elemento
 */
public abstract class AbstractService<M, D, K> {
     protected final Repository<M, K> repository;

    protected AbstractService(Repository<M, K> repository) {
        this.repository = repository;
    }

    protected abstract D toDto(M model);
    protected abstract M toModel(D dto);

    protected D add(D dto) {
        if (dto == null) throw new IllegalArgumentException("DTO cannot be null");

        M saved = repository.create(toModel(dto));
        return toDto(saved);
    }

    public D update(D dto) {
        if (dto == null) throw new IllegalArgumentException("DTO cannot be null");

        M updated = repository.update(toModel(dto));
        return toDto(updated);
    }

    public D get(K id) {
         return toDto(repository.get(id));
    }

    public D remove(K id) {
        return toDto(repository.remove(id));
    }

    public List<D> getAll() {
        return new ArrayList<>(
                repository.getAll()
                        .stream()
                        .map(this::toDto)
                        .toList()
        );
    }

    public boolean existsId(K id) {
        return repository.existsId(id);
    }

    public int getSize() {
        return repository.getSize();
    }

}
