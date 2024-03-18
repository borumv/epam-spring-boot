package org.vlasevsky.gym.dao;
import lombok.extern.slf4j.Slf4j;
import org.vlasevsky.gym.model.BaseEntity;
import org.vlasevsky.gym.storage.Storage;
import java.util.Optional;

@Slf4j
public abstract class BaseAbstractDAO<T extends BaseEntity> implements BaseDao<T> {

    Storage<T> storage;

    public Optional<T> findById(Long id) {
        Optional<T> result = storage.findById(id);
        log.info("findById called with id: {}, result: {}", id, result);
        return result;
    }

    public T save(T entity) {
        T savedEntity = storage.save(entity);
        log.info("Entity saved: {}", savedEntity);
        return savedEntity;
    }
}
