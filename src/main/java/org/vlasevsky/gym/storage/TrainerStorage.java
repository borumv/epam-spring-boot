package org.vlasevsky.gym.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vlasevsky.gym.model.Trainer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TrainerStorage implements Storage<Trainer>{

    Map<Long, Trainer> trainerStorage = new HashMap<>();

    @Autowired
    public TrainerStorage (StorageDataService storageDataService) {
        List<Trainer> trainees = Arrays.asList(storageDataService.getStorageData().getTrainers());
        trainerStorage.putAll(trainees.stream()
                .collect(Collectors.toMap(Trainer::getId, trainee -> trainee)));
    }
    public Map<Long, Trainer> getAllData() {
        return trainerStorage;
    }
}
