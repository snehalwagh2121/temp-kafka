package com.testcamel.kafkametrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaMetricsApplication {
    public static void main(String[] args) {
        SpringApplication.run(KafkaMetricsApplication.class, args);
    }

    @Autowired
    private MeterRegistry meterRegistry;

}
