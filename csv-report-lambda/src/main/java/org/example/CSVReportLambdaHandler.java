package org.example;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class CSVReportLambdaHandler implements RequestHandler<Object, String> {

    private static final String TABLE_NAME = "trainer_workload_summary";
    private static final String BUCKET_NAME = "borisvlasevsky-webapp-bucket";
    private static final String REGION = "us-east-1";

    private static final AmazonDynamoDB dynamoDBClient = AmazonDynamoDBClientBuilder.standard()
            .withRegion(REGION)
            .build();

    private static final DynamoDB dynamoDB = new DynamoDB(dynamoDBClient);

    private static final AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
            .withRegion(REGION)
            .build();

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String handleRequest(Object input, Context context) {
        context.getLogger().log("Starting CSVReportLambdaHandler...");

        try {
            List<TrainerData> trainerDataList = fetchTrainerData(context);

            if (trainerDataList.isEmpty()) {
                context.getLogger().log("No trainer data to process.");
                return "Success";
            }

            ByteArrayOutputStream csvData = generateCSVReport(trainerDataList, context);

            uploadToS3(csvData, context);

            return "Success";

        } catch (Exception e) {
            context.getLogger().log("Exception: " + e.getMessage());
            return "Error";
        }
    }

    private List<TrainerData> fetchTrainerData(Context context) throws Exception {
        Table table = dynamoDB.getTable(TABLE_NAME);
        ItemCollection<ScanOutcome> items = table.scan(new ScanSpec());

        LocalDate now = LocalDate.now(ZoneId.of("UTC"));
        int currentYear = now.getYear();
        int currentMonth = now.getMonthValue();

        List<Item> itemsList = new ArrayList<>();
        items.forEach(itemsList::add);

        return itemsList.stream()
                .map(item -> parseTrainerData(item, currentYear, currentMonth, context))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private TrainerData parseTrainerData(Item item, int currentYear, int currentMonth, Context context) {
        String firstName = item.getString("firstName");
        String lastName = item.getString("lastName");
        boolean isActive = item.getBoolean("isActive");
        String yearlySummariesJson = item.getString("yearlySummaries");

        try {
            YearlyTrainingSummary[] yearlySummaries = objectMapper.readValue(yearlySummariesJson, YearlyTrainingSummary[].class);

            int currentMonthDuration = Arrays.stream(yearlySummaries)
                    .filter(yts -> yts.getTrainingYear() == currentYear)
                    .map(yts -> yts.getMonthlySummary().getOrDefault(currentMonth, 0))
                    .findFirst()
                    .orElse(0);

            if (!isActive && currentMonthDuration == 0) {
                context.getLogger().log("Skip the trainer " + firstName + " " + lastName + ": inactive and there are no trainings in the current month.");
            }

            return new TrainerData(firstName, lastName, currentMonthDuration);

        } catch (Exception e) {
            context.getLogger().log("Error parsing yearlySummaries for trainer " + firstName + " " + lastName + ": " + e.getMessage());
            return null;
        }
    }

    private ByteArrayOutputStream generateCSVReport(List<TrainerData> trainerDataList, Context context) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (OutputStreamWriter osw = new OutputStreamWriter(baos, StandardCharsets.UTF_8);
             CSVPrinter csvPrinter = new CSVPrinter(osw, CSVFormat.DEFAULT
                     .withHeader("Trainer First Name", "Trainer Last Name", "Current Month Training Duration"))) {

            trainerDataList.forEach(trainerData -> {
                try {
                    csvPrinter.printRecord(trainerData.getFirstName(), trainerData.getLastName(), trainerData.getCurrentMonthDuration());
                } catch (Exception e) {
                    context.getLogger().log("Ошибка при записи данных тренера " + trainerData.getFirstName() + " " + trainerData.getLastName() + ": " + e.getMessage());
                }
            });

            csvPrinter.flush();
        }
        return baos;
    }

    private void uploadToS3(ByteArrayOutputStream csvData, Context context) throws AmazonServiceException, SdkClientException {
        LocalDate now = LocalDate.now(ZoneId.of("UTC"));
        int currentYear = now.getYear();
        int currentMonth = now.getMonthValue();

        String reportName = String.format("Trainers_Trainings_summary_%d_%02d.csv", currentYear, currentMonth);

        ByteArrayInputStream bais = new ByteArrayInputStream(csvData.toByteArray());

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(csvData.size());
        metadata.setContentType("text/csv");

        s3Client.putObject(BUCKET_NAME, reportName, bais, metadata);

        context.getLogger().log("CSV report uploaded successfully to S3 bucket: " + BUCKET_NAME + ", with key: " + reportName);
    }

    static class TrainerData {
        private final String firstName;
        private final String lastName;
        private final int currentMonthDuration;

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
