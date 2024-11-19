package com.example.workloadservice.mq;

import com.example.workloadservice.dto.TrainerWorkloadRequest;
import com.example.workloadservice.service.TrainerWorkloadService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.workloadservice.config.JmsConstants.WORKLOAD_QUEUE;

@Service
@Slf4j
public class TrainerWorkloadListener {

    @Autowired
    private TrainerWorkloadService trainerWorkloadService;




    @SqsListener("Gym-Queue")
    public void receiveWorkloadRequest(TrainerWorkloadRequest request) {
        log.info("TransactionId: {} - Received message from {}: {}", request.getTransactionId(), WORKLOAD_QUEUE, request);
        try {
            log.info("TransactionId: {} - Processing workload for trainer: {}", request.getTransactionId(), request.getUsername());
            trainerWorkloadService.updateWorkload(request);
            log.info("TransactionId: {} - Successfully processed workload for trainer: {}", request.getTransactionId(), request.getUsername());
        } catch (Exception e) {
            log.error("TransactionId: {} - Failed to process workload for trainer: {}. Sending to dead letter queue.", request.getTransactionId(), request.getUsername(), e);
        }
    }
}
