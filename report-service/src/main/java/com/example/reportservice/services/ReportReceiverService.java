package com.example.reportservice.services;

import com.example.reportservice.repository.ReportRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReportReceiverService {

    @Autowired
    private ReportRepository reportRepository;

//    @JmsListener(destination = "report.queue")
//    public void receiveReport(Message message) throws JMSException, JsonProcessingException {
//        String reportJSON = ((TextMessage) message).getText();
//        ObjectMapper objectMapper = new ObjectMapper();
//        ReportDTO reportDTO = objectMapper.readValue(reportJSON, ReportDTO.class);
//
//        log.info("Received report: {}", reportDTO);
//        Report report = new Report();
//        report.setContent(reportDTO.getContent());
//        report.setSourceService(reportDTO.getSourceService());
//        report.setTimestamp(reportDTO.getTimestamp());
//
//        reportRepository.save(report);
//    }
}
