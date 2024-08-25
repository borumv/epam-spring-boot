package com.example.workloadservice.mq;

import com.example.workloadservice.config.JmsConstants;
import com.example.workloadservice.dto.TrainerWorkloadRequest;
import com.example.workloadservice.model.TrainerWorkloadSummary;
import com.example.workloadservice.service.TrainerWorkloadService;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.ObjectMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class TrainerWorkloadListener {

    @Autowired
    private TrainerWorkloadService trainerWorkloadService;
    @Autowired
    private JmsTemplate jmsTemplate;

    private static final String WORKLOAD_QUEUE = JmsConstants.WORKLOAD_QUEUE;
    private static final String DEAD_LETTER_QUEUE = JmsConstants.DEAD_LETTER_QUEUE;
    private static final String WORKLOAD_REQUEST_QUEUE = JmsConstants.WORKLOAD_REQUEST_QUEUE;

    @JmsListener(destination = WORKLOAD_QUEUE)
    public void receiveWorkloadRequest(TrainerWorkloadRequest request) {
        log.info("Received message from {}: {}", WORKLOAD_QUEUE, request);
        try {
            log.info("Processing workload for trainer: {}", request.getUsername());
            trainerWorkloadService.updateWorkload(request);
            log.info("Successfully processed workload for trainer: {}", request.getUsername());
        } catch (Exception e) {
            log.error("Failed to process workload for trainer: {}. Sending to dead letter queue.", request.getUsername(), e);
            jmsTemplate.convertAndSend(DEAD_LETTER_QUEUE, request);
        }
    }

    @JmsListener(destination = WORKLOAD_REQUEST_QUEUE)
    public void receiveWorkloadRequest(Message message) {
        log.info("Received message from {}", WORKLOAD_REQUEST_QUEUE);

        if (message instanceof ObjectMessage) {
            processObjectMessage((ObjectMessage) message);
        } else {
            log.warn("Received unsupported message type: {}", message.getClass().getName());
        }
    }

    private void processObjectMessage(ObjectMessage objectMessage) {
        Map<String, Object> requestPayload = extractRequestPayload(objectMessage);
        if (requestPayload == null) return;

        String username = (String) requestPayload.get("username");
        int year = (int) requestPayload.get("year");
        int month = (int) requestPayload.get("month");

        log.info("Processing request for username: {}, year: {}, month: {}", username, year, month);

        TrainerWorkloadSummary summary = trainerWorkloadService.getWorkload(username, year, month);

        log.info("Workload summary received: {}.", summary);

        sendResponse(objectMessage, summary);
    }

    private Map<String, Object> extractRequestPayload(ObjectMessage objectMessage) {
        try {
            return (Map<String, Object>) objectMessage.getObject();
        } catch (JMSException e) {
            log.error("Failed to extract request payload from message: {}", e.getMessage(), e);
            return null;
        }
    }

    private void sendResponse(ObjectMessage message, TrainerWorkloadSummary summary) {
        try {
            jmsTemplate.convertAndSend(message.getJMSReplyTo(), summary);
            log.info("Response sent to {}", message.getJMSReplyTo());
        } catch (JMSException e) {
            log.error("Failed to send response: {}", e.getMessage(), e);
        }
    }
}
