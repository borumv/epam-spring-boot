package org.vlasevsky.gym.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

public interface BaseEntity <T extends Serializable> {

    void setId(T id);

    T getId();

}