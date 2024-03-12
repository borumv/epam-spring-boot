package org.vlasevsky.gym.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.vlasevsky.gym.model.StorageData;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
public class StorageDataService {

    private StorageData storageData;

    @Value("${storage.data.path}")
    private Resource dataResource;

    @PostConstruct
    public void init() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        storageData = objectMapper.readValue(dataResource.getInputStream(), StorageData.class);
    }

    public StorageData getStorageData() {
        return storageData;
    }
}