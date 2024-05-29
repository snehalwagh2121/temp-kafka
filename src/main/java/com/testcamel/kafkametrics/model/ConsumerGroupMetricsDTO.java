package com.testcamel.kafkametrics.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerGroupMetricsDTO {
    private String groupId;
    private String memberId;
    private String clientId;
    private String host;
    private String topic;
    private int partition;
    private long committedOffset;
    private long lag;
}
