package com.example.reportservice.controllers;

import com.example.reportservice.model.Report;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

//    @Autowired
//    private ReportRepository reportRepository;

    @GetMapping
    public ResponseEntity<List<Report>> getAllReports() {
        //List<Report> reports = reportRepository.findAll();
        return ResponseEntity.ok(List.of(new Report()));
    }
}