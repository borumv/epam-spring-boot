package com.vlasevsky.gym.service.map;

import com.example.common.dto.ReportDTO;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ReportSenderService {

//    @Autowired
//    private JmsTemplate jmsTemplate;

    public void sendReport(String content) {
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setContent(content);
        reportDTO.setSourceService("gym-service");
        reportDTO.setTimestamp(Instant.now().toString());

//        jmsTemplate.convertAndSend("report.queue", reportDTO, message -> {
//            message.setStringProperty("_type", "gymReport");
//            return message;
//        });
    }
}