package com.example.reportservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReportReceiverService {

//    @Autowired
//    private ReportRepository reportRepository;

    @SqsListener("Gym-Queue")
    public void receiveReport(String message) throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        ReportDTO reportDTO = objectMapper.readValue(message, ReportDTO.class);

        log.info("Received report: {}", message);
//        Report report = new Report();
//        report.setContent(reportDTO.getContent());
//        report.setSourceService(reportDTO.getSourceService());
//        report.setTimestamp(reportDTO.getTimestamp());

        //reportRepository.save(report);
    }
}
