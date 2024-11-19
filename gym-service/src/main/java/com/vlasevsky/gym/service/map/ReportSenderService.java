package com.vlasevsky.gym.service.map;

import com.example.common.dto.ReportDTO;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.time.Instant;

@Service
@Slf4j
public class ReportSenderService {

    @Value("${cloud.aws.sqs.queue.url}")
    private String queueURL;

    @Autowired
    private final SqsClient sqsClient;


    @Autowired
    public ReportSenderService(@Value("${cloud.aws.sqs.queue.url}") String queueURL, SqsClient sqsClient) {
        this.queueURL = queueURL;
        this.sqsClient = sqsClient;
    }

    public void sendReport(String content) {
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setContent(content);
        reportDTO.setSourceService("gym-service");
        reportDTO.setTimestamp(Instant.now().toString());
        log.info("Sending report");
        var sendMsgRequest = SendMessageRequest.builder().queueUrl(queueURL).messageBody(reportDTO.toString()).build();
        var response = sqsClient.sendMessage(sendMsgRequest);
        //  sqsTemplate.send("Gym-Queue", reportDTO);

        log.info("Message sent successfully. MessageId: {}", response.messageId());
    }
}
