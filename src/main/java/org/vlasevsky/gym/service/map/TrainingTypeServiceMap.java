package org.vlasevsky.gym.service.map;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vlasevsky.gym.dao.TrainingTypeRepository;
import org.vlasevsky.gym.model.TrainingType;
import org.vlasevsky.gym.service.TrainingTypeService;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class TrainingTypeServiceMap implements TrainingTypeService {

    private TrainingTypeRepository trainingTypeRepository;

    @Override
    @Transactional
    public List<TrainingType> findAll() {
        return trainingTypeRepository.findAll();
    }
}
