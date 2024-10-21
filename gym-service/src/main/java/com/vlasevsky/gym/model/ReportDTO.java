package com.vlasevsky.gym.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "_type")
public class ReportDTO {
    private String id;
    private String content;
    private String sourceService;
    private String timestamp;
}