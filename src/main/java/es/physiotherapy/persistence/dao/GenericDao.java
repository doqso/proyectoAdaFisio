package es.physiotherapy.persistence.dao;

import java.util.Optional;

public interface GenericDao<T, R> {
    T create(T entity);

    void save(T entity);

    void deleteById(R id);

    void delete(T entity);

    Optional<T> findById(R id);
}
