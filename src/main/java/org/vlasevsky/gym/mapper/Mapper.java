package org.vlasevsky.gym.mapper;

public interface Mapper<F,T> {
    T mapFrom(F object);
}
