package com.testcamel.kafkametrics.controller;

import com.testcamel.kafkametrics.model.ConsumerGroupMetricsDTO;
import com.testcamel.kafkametrics.model.MetricsResult;
import com.testcamel.kafkametrics.model.PartitionInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.requests.OffsetFetchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/kafka-metrics")
@Slf4j
public class KafkaMetricsController {

    @Autowired
    private AdminClient adminClient;

    @GetMapping("/topics-info")
    public ResponseEntity<MetricsResult> getTopics() throws ExecutionException, InterruptedException {
        log.info("started executing metrics");
        MetricsResult metricsResult=new MetricsResult(new ArrayList<>(), new ArrayList<>());
        DescribeTopicsResult topicsResult = adminClient.describeTopics(adminClient.listTopics().names().get());
        Map<String, TopicDescription> topicDescriptions = topicsResult.allTopicNames().get();
        for (Map.Entry<String, TopicDescription> entry : topicDescriptions.entrySet()) {
            String topicName = entry.getKey();
            TopicDescription topicDescription = entry.getValue();
            List<PartitionInfoDTO> topicPartitionInfo = topicDescription.partitions().stream()
                    .map(partition -> new PartitionInfoDTO(
                            topicName,
                            partition.partition(),
                            partition.leader().id(),
                            partition.replicas().stream().map(Node::id).collect(Collectors.toList()),
                            partition.isr().stream().map(Node::id).collect(Collectors.toList())
                    ))
                    .toList();
            metricsResult.getPartitionInfoDTO().addAll(topicPartitionInfo);
        }
        log.info("partions info collected");
        Collection<String> consumerGroupIds = adminClient.listConsumerGroups().all().get().stream()
                .map(ConsumerGroupListing::groupId)
                .toList();
        DescribeConsumerGroupsResult consumerGroupsResult = adminClient.describeConsumerGroups(consumerGroupIds);
        Map<String, ConsumerGroupDescription> consumerGroups = consumerGroupsResult.all().get();
        for (Map.Entry<String, ConsumerGroupDescription> entry : consumerGroups.entrySet()) {
            String groupId = entry.getKey();
            ConsumerGroupDescription groupDescription = entry.getValue();

            // Fetch committed offsets for each consumer group
            List<TopicPartition> partitions = groupDescription.members().stream()
                    .flatMap(member -> member.assignment().topicPartitions().stream())
                    .collect(Collectors.toList());
            if (!partitions.isEmpty()) {
                Map<TopicPartition, OffsetAndMetadata> offsets = adminClient
                        .listConsumerGroupOffsets(groupId)
                        .partitionsToOffsetAndMetadata()
                        .get();

                // Fetch end offsets for each partition
                Map<TopicPartition, ListOffsetsResult.ListOffsetsResultInfo> endOffsets = adminClient.listOffsets(partitions.stream()
                        .collect(Collectors.toMap(tp -> tp, tp -> OffsetSpec.latest()))).all().get();

                for (MemberDescription member : groupDescription.members()) {
                    for (TopicPartition tp : member.assignment().topicPartitions()) {
                        long committedOffset = offsets.get(tp).offset();
                        long endOffset = endOffsets.get(tp).offset();
                        long lag = endOffset - committedOffset;

                        metricsResult.getConsumerGroupMetricsDTO().add(new ConsumerGroupMetricsDTO(
                                groupId,
                                member.consumerId(),
                                member.clientId(),
                                member.host(),
                                tp.topic(),
                                tp.partition(),
                                committedOffset,
                                lag
                        ));
                    }
                }
            }
        }
        log.info("consumer metrics info collected");
        return new ResponseEntity<>(metricsResult, HttpStatus.OK);
    }

}
