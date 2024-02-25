package org.vlasevsky.gym.service;

public interface BaseService <T, ID>{

    T findById(ID id);
    T save(T person);
}
