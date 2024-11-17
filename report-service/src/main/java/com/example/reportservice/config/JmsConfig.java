//package com.example.reportservice.config;
//
//
//import com.example.reportservice.model.ReportDTO;
//import jakarta.jms.ConnectionFactory;
//import org.apache.activemq.ActiveMQConnectionFactory;
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
//public class JmsConfig {
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
//}