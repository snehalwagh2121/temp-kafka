package com.testcamel.kafkametrics.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartitionInfoDTO {
    private String topic;
    private int partition;
    private int leader;
    private List<Integer> replicas;
    private List<Integer> isr;

//    public PartitionInfoDTO(String topic, int partition, int leader, List<Integer> replicas, List<Integer> isr) {
//        this.topic = topic;
//        this.partition = partition;
//        this.leader = leader;
//        this.replicas = replicas;
//        this.isr = isr;
//    }

    @Override
    public String toString() {
        return "PartitionInfoDTO{" +
                "topic='" + topic + '\'' +
                ", partition=" + partition +
                ", leader=" + leader +
                ", replicas=" + replicas +
                ", isr=" + isr +
                '}';
    }
}
