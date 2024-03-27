package org.vlasevsky.gym.dao;

import org.vlasevsky.gym.model.BaseEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface BaseDao<K extends Serializable, T extends BaseEntity> {

    T save(T entity);

    void delete(K id);

    void update(T entity);
    Optional<T> findById(K id);

    List<T> findAll();

}
