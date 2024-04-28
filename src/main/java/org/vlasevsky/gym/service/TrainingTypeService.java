package org.vlasevsky.gym.service;

import org.vlasevsky.gym.model.TrainingType;

import java.util.List;

public interface TrainingTypeService extends BaseService<TrainingType, Long>  {

    List<TrainingType> findAll();
}


