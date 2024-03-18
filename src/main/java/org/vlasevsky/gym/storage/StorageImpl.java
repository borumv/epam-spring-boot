package org.vlasevsky.gym.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.vlasevsky.gym.model.*;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Slf4j
public class StorageImpl<T extends BaseEntity> implements Storage<T> {
    private final Map<Long, T> storage = new HashMap<>();
    private final Class<T> type;

    @Value("${storage.data.path}")
    private Resource dataResource;

    public StorageImpl(Class<T> type) {
        this.type = type;
    }

    @Override
    public Optional<T> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Collection<T> findAll() {
        return storage.values();
    }

    @Override
    public T save(T entity) {
        if (entity != null) {
            if (entity.getId() == null) {
                entity.setId(getNextId());
            }
            storage.put(entity.getId(), entity);
        } else {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        log.info("Entity saved: {}", entity);
        return entity;
    }

    private Long getNextId() {
        Long nextId = null;
        try {
            nextId = Collections.max(storage.keySet()) + 1L;
        } catch (NoSuchElementException e) {
            log.debug("Storage is empty, initializing nextId to 1");
            nextId = 1L;
        }
        return nextId;
    }

    @Override
    public void delete(Long id) {
        storage.remove(id);
        log.info("Entity with id {} deleted", id);
    }

    @PostConstruct
    public void init() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            StorageData storageData = objectMapper.readValue(dataResource.getFile(), StorageData.class);
            List<T> entities = null;
            if (type == Trainee.class) {
                entities = (List<T>) storageData.getTrainees();
            } else if (type == Trainer.class) {
                entities = (List<T>) storageData.getTrainers();
            } else if (type == Training.class) {
                entities = (List<T>) storageData.getTrainings();
            }
            if (entities != null) {
                for (T entity : entities) {
                    save(entity);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
