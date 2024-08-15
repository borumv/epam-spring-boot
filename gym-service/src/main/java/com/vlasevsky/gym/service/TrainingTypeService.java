package com.vlasevsky.gym.service;

import com.vlasevsky.gym.model.TrainingType;

import java.util.List;

public interface TrainingTypeService extends BaseService<TrainingType, Long>  {

    List<TrainingType> findAll();
}


