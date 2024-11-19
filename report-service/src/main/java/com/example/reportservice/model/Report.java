package com.example.reportservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@Document(collection = "reports")
public class Report {
   // @Id
    private String id;
    private String content;
    private String sourceService;
    private String timestamp;
}