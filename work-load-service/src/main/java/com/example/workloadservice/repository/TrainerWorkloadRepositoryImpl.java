package com.example.workloadservice.repository;

import com.example.workloadservice.exception.TrainerWorkloadNotFoundException;
import com.example.workloadservice.model.TrainerWorkloadSummary;
import com.example.workloadservice.model.YearlyTrainingSummary;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TrainerWorkloadRepositoryImpl implements TrainerWorkloadRepository {
    private static final String TABLE_NAME = "trainer_workload_summary";

    @Autowired
    private DynamoDbClient dynamoDbClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void save(TrainerWorkloadSummary summary) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("username", AttributeValue.builder().s(summary.getUsername()).build());
        item.put("firstName", AttributeValue.builder().s(summary.getFirstName()).build());
        item.put("lastName", AttributeValue.builder().s(summary.getLastName()).build());
        item.put("isActive", AttributeValue.builder().bool(summary.getIsActive()).build());

        // Сериализация yearlySummaries в JSON строку
        try {
            String yearlySummariesJson = objectMapper.writeValueAsString(summary.getYearlySummaries());
            item.put("yearlySummaries", AttributeValue.builder().s(yearlySummariesJson).build());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing yearlySummaries", e);
        }

        PutItemRequest request = PutItemRequest.builder()
                .tableName(TABLE_NAME)
                .item(item)
                .build();

        dynamoDbClient.putItem(request);
    }

    @Override
    public TrainerWorkloadSummary findByUsername(String username) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("username", AttributeValue.builder().s(username).build());

        GetItemRequest request = GetItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(key)
                .consistentRead(true)
                .build();

        GetItemResponse response = dynamoDbClient.getItem(request);
        Map<String, AttributeValue> item = response.item();

        if (item == null || item.isEmpty()) {
            throw new TrainerWorkloadNotFoundException(username);
        }

        TrainerWorkloadSummary summary = new TrainerWorkloadSummary();
        summary.setUsername(item.get("username").s());
        summary.setFirstName(item.get("firstName").s());
        summary.setLastName(item.get("lastName").s());
        summary.setIsActive(item.get("isActive").bool());

        // Десериализация yearlySummaries из JSON строки
        String yearlySummariesJson = item.get("yearlySummaries").s();
        try {
            YearlyTrainingSummary[] yearlySummariesArray = objectMapper.readValue(yearlySummariesJson, YearlyTrainingSummary[].class);
            summary.setYearlySummaries(List.of(yearlySummariesArray));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing yearlySummaries", e);
        }

        return summary;
    }
}
