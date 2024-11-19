package org.example;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CSVReportLambdaHandler implements RequestHandler<Object, String> {

    private static final String TABLE_NAME = "trainer_workload_summary";
    private static final String BUCKET_NAME = "borisvlasevsky-webapp-bucket";
    private static final String REGION = "us-east-1";

    @Override
    public String handleRequest(Object input, Context context) {
        context.getLogger().log("Starting CSVReportLambdaHandler...");

        AmazonDynamoDB dynamoDBClient = null;
        AmazonS3 s3Client = null;
        try {
            dynamoDBClient = AmazonDynamoDBClientBuilder.standard()
                    .withRegion(REGION)
                    .build();

            DynamoDB dynamoDB = new DynamoDB(dynamoDBClient);
            ObjectMapper objectMapper = new ObjectMapper();

            Table table = dynamoDB.getTable(TABLE_NAME);

            ScanSpec scanSpec = new ScanSpec();
            ItemCollection<ScanOutcome> items = table.scan(scanSpec);

            // Prepare data for CSV
            List<TrainerData> trainerDataList = new ArrayList<>();

            LocalDate now = LocalDate.now(ZoneId.of("UTC"));
            int currentYear = now.getYear();
            int currentMonth = now.getMonthValue();

            for (Item item : items) {
                String firstName = item.getString("firstName");
                String lastName = item.getString("lastName");
                boolean isActive = item.getBoolean("isActive");
                String yearlySummariesJson = item.getString("yearlySummaries");

                try {
                    YearlyTrainingSummary[] yearlySummaries = objectMapper.readValue(yearlySummariesJson, YearlyTrainingSummary[].class);

                    int currentMonthDuration = 0;
                    for (YearlyTrainingSummary yts : yearlySummaries) {
                        if (yts.getTrainingYear() == currentYear) {
                            Integer monthDuration = yts.getMonthlySummary().get(currentMonth);
                            if (monthDuration != null) {
                                currentMonthDuration = monthDuration;
                                break;
                            }
                        }
                    }

                    if (!isActive && currentMonthDuration == 0) {
                        continue;
                    }

                    trainerDataList.add(new TrainerData(firstName, lastName, currentMonthDuration));

                } catch (Exception e) {
                    context.getLogger().log("Error parsing yearlySummaries for trainer " + firstName + " " + lastName + ": " + e.getMessage());
                }
            }

            // Generate CSV
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(baos, StandardCharsets.UTF_8);
                CSVPrinter csvPrinter = new CSVPrinter(osw, CSVFormat.DEFAULT
                        .withHeader("Trainer First Name", "Trainer Last Name", "Current Month Training Duration"));

                for (TrainerData trainerData : trainerDataList) {
                    csvPrinter.printRecord(trainerData.getFirstName(), trainerData.getLastName(), trainerData.getCurrentMonthDuration());
                }

                csvPrinter.flush();

                // Initialize S3 client
                s3Client = AmazonS3ClientBuilder.standard()
                        .withRegion(REGION)
                        .build();

                String reportName = String.format("Trainers_Trainings_summary_%d_%02d.csv", currentYear, currentMonth);

                ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

                // Set metadata with content length
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(baos.size());
                metadata.setContentType("text/csv");

                // Upload to S3
                s3Client.putObject(BUCKET_NAME, reportName, bais, metadata);

                context.getLogger().log("CSV report uploaded successfully to S3 bucket: " + BUCKET_NAME + ", with key: " + reportName);

            } catch (AmazonServiceException e) {
                context.getLogger().log("AmazonServiceException: " + e.getMessage());
                return "Error";
            } catch (SdkClientException e) {
                context.getLogger().log("SdkClientException: " + e.getMessage());
                return "Error";
            } catch (Exception e) {
                context.getLogger().log("Exception: " + e.getMessage());
                return "Error";
            }

        } catch (AmazonServiceException e) {
            context.getLogger().log("AmazonServiceException: " + e.getMessage());
            return "Error";
        } catch (SdkClientException e) {
            context.getLogger().log("SdkClientException: " + e.getMessage());
            return "Error";
        } catch (Exception e) {
            context.getLogger().log("Exception: " + e.getMessage());
            return "Error";
        }

        return "Success";
    }

    // Helper class to hold trainer data
    static class TrainerData {
        private String firstName;
        private String lastName;
        private int currentMonthDuration;

        public TrainerData(String firstName, String lastName, int currentMonthDuration) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.currentMonthDuration = currentMonthDuration;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public int getCurrentMonthDuration() {
            return currentMonthDuration;
        }
    }

    // YearlyTrainingSummary class
    static class YearlyTrainingSummary {
        private int trainingYear;
        private Map<Integer, Integer> monthlySummary;

        public int getTrainingYear() {
            return trainingYear;
        }

        public void setTrainingYear(int trainingYear) {
            this.trainingYear = trainingYear;
        }

        public Map<Integer, Integer> getMonthlySummary() {
            return monthlySummary;
        }

        public void setMonthlySummary(Map<Integer, Integer> monthlySummary) {
            this.monthlySummary = monthlySummary;
        }
    }
}
