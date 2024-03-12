package org.vlasevsky.gym.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vlasevsky.gym.model.Trainee;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TraineeStorage implements Storage<Trainee> {


    Map<Long, Trainee> traineeStorage = new HashMap<>();

    @Autowired
    public TraineeStorage(StorageDataService storageDataService) {
        List<Trainee> trainees = Arrays.asList(storageDataService.getStorageData().getTrainees());
        traineeStorage.putAll(trainees.stream()
                .collect(Collectors.toMap(Trainee::getId, trainee -> trainee)));
    }

    public Map<Long, Trainee> getAllData() {
        return traineeStorage;
    }
}
