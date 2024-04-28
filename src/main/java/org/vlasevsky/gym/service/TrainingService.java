package org.vlasevsky.gym.service;

import org.vlasevsky.gym.dto.CredentialsDto;
import org.vlasevsky.gym.dto.TrainingCreateDto;
import org.vlasevsky.gym.model.Training;

public interface TrainingService extends BaseService<Training, Long> {

    void create(TrainingCreateDto createDto);
}
