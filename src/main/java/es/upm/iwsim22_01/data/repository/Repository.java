package es.upm.iwsim22_01.data.repository;

import java.util.Optional;
import java.util.stream.Stream;

public interface Repository<T, K> {
    <T2 extends T> T2 create(T2 element);
    
    T get(K id);
    
    Stream<T> getAll();
    
    T update(T element);
    
    T remove(K id);

    boolean existsId(K id);

    int getSize();
}
