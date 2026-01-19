package es.upm.iwsim22_01.data.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import es.upm.iwsim22_01.data.repository.adapters.DateAdapter;
import es.upm.iwsim22_01.data.repository.adapters.LocalDateTimeAdapter;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Implementación base de un repositorio genérico persistido en fichero JSON.
 *
 * Gestiona automáticamente la carga, almacenamiento en caché y persistencia
 * de los elementos, delegando en las subclases la definición del identificador,
 * la ruta del fichero y el tipo de datos almacenados.
 *
 * @param <T> tipo de los elementos almacenados en el repositorio
 * @param <K> tipo del identificador único de cada elemento
 */
public abstract class AbstractRepository<T, K> implements Repository<T, K> {
    protected static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Date.class, new DateAdapter())
            .create();

    protected Map<K, T> cache;

    /**
     * Devuelve la ruta del fichero donde se almacenan los datos.
     *
     * @return ruta del fichero de persistencia
     */
    protected abstract String getFilePath();

    /**
     * Obtiene el identificador único de un elemento.
     *
     * @param element elemento del repositorio
     * @return identificador único del elemento
     */
    protected abstract K getId(T element);

    /**
     * Devuelve el TypeToken necesario para la deserialización
     * de la lista de elementos.
     *
     * @return TypeToken correspondiente a List<T>
     */
    protected abstract TypeToken<List<T>> getTypeToken();

    /**
     * Obtiene el fichero de persistencia, creándolo junto con
     * sus directorios padre si no existen.
     *
     * Si el fichero se crea por primera vez, se inicializa con
     * una lista vacía en formato JSON.
     *
     * @return fichero de persistencia
     * @throws IOException si ocurre un error de acceso al sistema de archivos
     */
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

    /**
     * Carga los datos del fichero en memoria si la caché
     * aún no ha sido inicializada.
     *
     * Convierte la lista deserializada en un mapa indexado
     * por identificador para facilitar las operaciones.
     */
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