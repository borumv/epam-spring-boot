package org.vlasevsky.gym.storage;

import org.vlasevsky.gym.model.BaseEntity;

import java.util.Map;

public interface Storage<T extends BaseEntity> {

    Map<Long, T> getAllData();
}
