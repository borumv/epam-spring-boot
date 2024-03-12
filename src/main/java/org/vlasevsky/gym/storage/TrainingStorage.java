package org.vlasevsky.gym.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vlasevsky.gym.model.Training;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TrainingStorage implements Storage<Training> {
    Map<Long, Training> trainingStorage = new HashMap<>();


    @Autowired
    public TrainingStorage(StorageDataService storageDataService) {
        List<Training> trainings = Arrays.asList(storageDataService.getStorageData().getTrainings());
        trainingStorage.putAll(trainings.stream()
                .collect(Collectors.toMap(Training::getId, trainee -> trainee)));
    }

    public Map<Long, Training> getAllData() {
        return trainingStorage;
    }
}
