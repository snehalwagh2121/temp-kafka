//package com.testcamel.kafkametrics;
//
//import io.micrometer.core.instrument.MeterRegistry;
//import io.micrometer.core.instrument.binder.kafka.KafkaClientMetrics;
//import org.apache.kafka.clients.consumer.Consumer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Nullable;
//import java.util.Map;
//import java.util.Properties;
//
//
//public class KafkaMetricsConsumerFactory<K, V> extends DefaultKafkaConsumerFactory<K, V> {
//
//    @Autowired
//    private MeterRegistry meters;
//
//    public KafkaMetricsConsumerFactory(Map<String, Object> configs) {
//        super(configs);
//    }
//
//    @Override
//    public Consumer<K, V> createConsumer(@Nullable String groupId, @Nullable String clientIdPrefix,
//                                         @Nullable String clientIdSuffix) {
//        Consumer<K, V> consumer = super.createConsumer(groupId, clientIdPrefix, clientIdSuffix, null);
//        new KafkaClientMetrics(consumer).bindTo(meters);
//        return consumer;
//    }
//
//    @Override
//    public Consumer<K, V> createConsumer(@Nullable String groupId, @Nullable String clientIdPrefix,
//                                         @Nullable final String clientIdSuffixArg, @Nullable Properties properties) {
//        Consumer<K, V> consumer = super.createConsumer(groupId, clientIdPrefix, clientIdSuffixArg, properties);
//        new KafkaClientMetrics(consumer).bindTo(meters);
//        return consumer;
//    }
//
//}
