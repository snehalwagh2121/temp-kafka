package com.testcamel.kafkametrics;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class Routes extends RouteBuilder {
    @Override
    public void configure() {

        from("kafka:bits-to-bytes-topic")
                .routeId("route1")
                .log("route1 hit")
                .to("kafka:bits-to-bytes-producer")
        ;
        from("kafka:bits-to-bytes-producer")
                .routeId("route2")
                .log("route2 hit")
                .to("kafka:bits-to-bytes-consumer")
        ;
    }
}
