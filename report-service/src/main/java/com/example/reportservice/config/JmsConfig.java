package com.example.reportservice.config;


import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class JmsConfig {



    @Bean
    public SqsAsyncClient sqsAsyncClient() {
        return SqsAsyncClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    @Bean
    public SqsTemplate sqsTemplate(SqsAsyncClient sqsAsyncClient) {
        return SqsTemplate.builder()
                .sqsAsyncClient(sqsAsyncClient)
                .build();
    }

    @Value("${cloud.aws.region.static}")
    private String awsRegion;

    @Bean
    public SqsClient sqsClient() {
        return SqsClient.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
//    @Bean
//    public MappingJackson2MessageConverter jacksonJmsMessageConverter() {
//        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
//        converter.setTargetType(MessageType.TEXT);
//        converter.setTypeIdPropertyName("_type");
//
//        Map<String, Class<?>> typeIdMappings = new HashMap<>();
//        typeIdMappings.put("gymReport", com.example.reportservice.model.ReportDTO.class);
//        converter.setTypeIdMappings(typeIdMappings);
//
//        return converter;
//    }
//    @Bean
//    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
//        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
//        jmsTemplate.setMessageConverter(messageConverter);
//        return jmsTemplate;
//    }
//
//    @Bean
//    public ActiveMQConnectionFactory connectionFactory() {
//        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
//        factory.setTrustAllPackages(true);
//        factory.setBrokerURL("tcp://activemq:61616");
//        return factory;
//    }
}