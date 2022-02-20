package com.whiskas68.cloud.util;

import com.whiskas68.cloud.configuration.KafkaTopicConfigurations;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class KafkaManage {

    @Autowired
    private KafkaTopicConfigurations configurations;

    @Autowired
    private KafkaAdmin admin;

    public CreatePartitionsResult increasePartitions(String topic,int num)throws Exception{

        AdminClient adminClient = AdminClient.create(admin.getConfigurationProperties());
        Map<String, NewPartitions> map = new HashMap<>();
        NewPartitions np = NewPartitions.increaseTo(num);
        map.put(topic, np);
        CreatePartitionsOptions cpo = new CreatePartitionsOptions();
        cpo.timeoutMs(5 * 1000);
        return adminClient.createPartitions(map, cpo);

        /*Short replicationFactor = 1;
        NewTopic topic1 = new NewTopic("jobs", 6 , replicationFactor);
        AtomicReference<Method> addTopics = new AtomicReference<>();
        AtomicReference<Method> modifyTopics = new AtomicReference<>();
        ReflectionUtils.doWithMethods(KafkaAdmin.class, m -> {
            m.setAccessible(true);
            if (m.getName().equals("addTopics")) {
                addTopics.set(m);
            }
            else if (m.getName().equals("modifyTopics")) {
                modifyTopics.set(m);
            }
        }, m -> {
            return m.getName().endsWith("Topics");
        });
        try (AdminClient adminClient = AdminClient.create(admin.getConfigurationProperties())) {
            addTopics.get().invoke(this.admin, adminClient, Collections.singletonList(topic1));
            modifyTopics.get().invoke(this.admin, adminClient, Collections.singletonMap(
                    topic1.name(), NewPartitions.increaseTo(topic1.numPartitions())));
        }*/
    }
}
