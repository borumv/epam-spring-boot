package org.vlasevsky.gym.dao;

import org.vlasevsky.gym.model.BaseEntity;

import java.util.Optional;

public interface BaseDao<T extends BaseEntity> {

    Optional<T> findById(Long id);

    T save(T entity);
}
