package com.example.reportservice.controllers;

import com.example.reportservice.model.Report;
import com.example.reportservice.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportRepository reportRepository;

    @GetMapping
    public ResponseEntity<List<Report>> getAllReports() {
        List<Report> reports = reportRepository.findAll();
        return ResponseEntity.ok(reports);
    }
}