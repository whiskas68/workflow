package com.whiskas68.cloud.configuration;

import lombok.AllArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.GenericWebApplicationContext;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
@AllArgsConstructor
public class KafkaTopicAdministrator {

    private final KafkaTopicConfigurations configurations;

    private final GenericWebApplicationContext context;

    @PostConstruct
    public void init(){
        initializeBeans(configurations.getTopics());
    }

    private void initializeBeans(List<KafkaTopicConfigurations.Topic> topics){
        topics.forEach(t -> context.registerBean(t.name, NewTopic.class, t::createNewTopic));
    }

}
