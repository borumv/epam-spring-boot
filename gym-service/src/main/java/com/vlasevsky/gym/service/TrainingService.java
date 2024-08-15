package com.vlasevsky.gym.service;


import com.vlasevsky.gym.dto.TrainingCreateDto;
import com.vlasevsky.gym.model.Training;

public interface TrainingService extends BaseService<Training, Long> {

    void create(TrainingCreateDto createDto);
}
