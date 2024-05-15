//package com.testcamel.kafkametrics;
//
//import io.micrometer.core.instrument.MeterRegistry;
//import io.micrometer.core.instrument.binder.kafka.KafkaClientMetrics;
//import org.apache.kafka.clients.producer.Producer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.core.DefaultKafkaProducerFactory;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//
//
//public class KafkaMetricsProducerFactory<K, V> extends DefaultKafkaProducerFactory<K, V> {
//
//    @Autowired
//    private MeterRegistry meters;
//
//    public KafkaMetricsProducerFactory(Map<String, Object> configs) {
//        super(configs);
//    }
//
//    @Override
//    protected Producer<K, V> createKafkaProducer() {
//        Producer producer = super.createKafkaProducer();
//        new KafkaClientMetrics(producer).bindTo(meters);
//        return producer;
//    }
//
//}