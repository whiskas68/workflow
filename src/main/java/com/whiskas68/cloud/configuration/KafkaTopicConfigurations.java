package com.whiskas68.cloud.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "kafka")
@Getter
@Setter
@ToString
public class KafkaTopicConfigurations {

    private List<Topic> topics;

    public Topic getTopicClass() {
        return new Topic();
    }

    @Getter
    @Setter
    @ToString
    static class Topic {
        String name;
        Integer numPartitions = 1;
        Short replicationFactor = 1;

        NewTopic createNewTopic() {
            return new NewTopic(this.name, this.numPartitions, this.replicationFactor);
        }

    }


}
