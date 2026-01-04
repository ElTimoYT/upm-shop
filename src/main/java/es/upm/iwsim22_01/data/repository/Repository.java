package es.upm.iwsim22_01.data.repository;

import java.util.List;

public interface Repository<T, K> {
    T create(T element);
    
    T get(K id);
    
    List<T> getAll();
    
    T update(T element);
    
    T remove(K id);

    boolean existsId(K id);

    int getSize();
}
