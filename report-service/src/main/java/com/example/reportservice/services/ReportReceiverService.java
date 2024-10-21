package com.example.reportservice.services;

import com.example.common.dto.ReportDTO;
import com.example.reportservice.model.Report;
import com.example.reportservice.repository.ReportRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
public class ReportReceiverService {

    @Autowired
    private ReportRepository reportRepository;

    @JmsListener(destination = "report.queue")
    public void receiveReport(Message message) throws JMSException, JsonProcessingException {
        String reportJSON = ((TextMessage) message).getText();
        ObjectMapper objectMapper = new ObjectMapper();
        ReportDTO reportDTO = objectMapper.readValue(reportJSON, ReportDTO.class);

        log.info("Received report: {}", reportDTO);
        Report report = new Report();
        report.setContent(reportDTO.getContent());
        report.setSourceService(reportDTO.getSourceService());
        report.setTimestamp(reportDTO.getTimestamp());

        reportRepository.save(report);
    }
}
