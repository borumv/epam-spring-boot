package org.vlasevsky.gym.model;

import lombok.Data;

import java.io.Serializable;

@Data
public abstract class BaseEntity implements Serializable {

    Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isNew() {
        return this.id == null;
    }
}