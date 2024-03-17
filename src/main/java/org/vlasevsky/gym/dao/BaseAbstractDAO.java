package org.vlasevsky.gym.dao;

import org.vlasevsky.gym.model.BaseEntity;
import org.vlasevsky.gym.storage.Storage;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

public abstract class BaseAbstractDAO<T extends BaseEntity> implements BaseDao<T> {


    Storage<T> storage;

    public Optional<T> findById(Long id) {
        return Optional.ofNullable(storage.getAllData().get(id));
    }

    public T save(T entity) {
        if (entity != null) {
            if (entity.getId() == null) {
                entity.setId(getNextId());
            }
            storage.getAllData().put(entity.getId(), entity);
        } else {
            System.out.println("Entity can't be null");
        }

        return entity;
    }

    public Long getNextId() {
        Long nextId = null;
        try {
            nextId = Collections.max(storage.getAllData().keySet()) + 1L;
        } catch (NoSuchElementException e) {
            nextId = 1L;
        }
        return nextId;
    }

}
