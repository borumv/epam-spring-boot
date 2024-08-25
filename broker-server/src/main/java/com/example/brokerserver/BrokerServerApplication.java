package com.example.brokerserver;

import org.apache.activemq.broker.BrokerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BrokerServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BrokerServerApplication.class, args);
    }

    @Beчan
    public BrokerService brokerService() throws Exception {
        BrokerService broker = new BrokerService();
        broker.addConnector("tcp://localhost:61616");
        broker.setPersistent(false);
        broker.setUseJmx(true);
        return broker;
    }
}
