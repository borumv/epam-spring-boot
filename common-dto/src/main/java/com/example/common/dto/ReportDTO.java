package com.example.common.dto;

import lombok.Data;

@Data
public class ReportDTO {
    private String id;
    private String content;
    private String sourceService;
    private String timestamp;
}