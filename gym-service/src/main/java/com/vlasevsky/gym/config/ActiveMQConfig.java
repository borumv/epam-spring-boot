//package com.vlasevsky.gym.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.vlasevsky.gym.dto.TrainerWorkloadRequest;
//import com.vlasevsky.gym.model.ReportDTO;
//import jakarta.jms.ConnectionFactory;
//import jakarta.jms.Queue;
//import org.apache.activemq.ActiveMQConnectionFactory;
//import org.apache.activemq.command.ActiveMQQueue;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jms.core.JmsTemplate;
//import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
//import org.springframework.jms.support.converter.MessageConverter;
//import org.springframework.jms.support.converter.MessageType;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//public class ActiveMQConfig {
//
//    @Bean
//    public Queue workloadQueue() {
//        return new ActiveMQQueue(JmsConstants.WORKLOAD_QUEUE);
//    }
//
//    @Bean
//    public Queue deadLetterQueue() {
//        return new ActiveMQQueue(JmsConstants.DEAD_LETTER_QUEUE);
//    }
//
//    @Bean
//    public Queue workloadResponseQueue() {
//        return new ActiveMQQueue(JmsConstants.WORKLOAD_RESPONSE_QUEUE);
//    }
//
//    @Bean
//    public MappingJackson2MessageConverter jacksonJmsMessageConverter() {
//        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
//        converter.setTargetType(MessageType.TEXT);
//        converter.setTypeIdPropertyName("_type"); // Указываем тип через _type
//
//        // Устанавливаем маппинг типов
//        Map<String, Class<?>> typeIdMappings = new HashMap<>();
//        typeIdMappings.put("gymReport", com.vlasevsky.gym.model.ReportDTO.class);
//        converter.setTypeIdMappings(typeIdMappings);
//
//        return converter;
//    }
//    @Bean
//    public ObjectMapper objectMapper() {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
//        return mapper;
//    }
//
//    @Bean
//    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
//        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
//        jmsTemplate.setMessageConverter(jacksonJmsMessageConverter());
//        jmsTemplate.setReceiveTimeout(JmsConstants.RECEIVE_TIMEOUT);
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
//}
