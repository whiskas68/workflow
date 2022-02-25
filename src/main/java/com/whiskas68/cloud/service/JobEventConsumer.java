package com.whiskas68.cloud.service;

import com.whiskas68.cloud.util.K8sClient;
import io.fabric8.kubernetes.api.model.batch.Job;
import io.fabric8.kubernetes.api.model.batch.JobCondition;
import io.fabric8.kubernetes.api.model.batch.JobStatus;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class JobEventConsumer {

    @Autowired
    private K8sClient k8sclient;

    @KafkaListener(topics = {"job-parallel"},groupId = "job-1",clientIdPrefix = "jobClient")
    public void execJobsFromKafkaParallel(ConsumerRecord<?,?> record, Acknowledgment acknowledgment) throws IOException{
        KubernetesClient client = k8sclient.getClient();
        Job job = (Job) record.value();
        String metaName = job.getMetadata().getName();
        client.batch().jobs().inNamespace("default").createOrReplace(job);
        log.info("K8s创建: ["+metaName+"]");
        acknowledgment.acknowledge();
    }

    @KafkaListener(topics = {"job-serial"},groupId = "job-1",clientIdPrefix = "jobClient")
    public void execJobsFromKafkaSerial(ConsumerRecord<?,?> record, Acknowledgment acknowledgment) throws IOException,InterruptedException{
        KubernetesClient client = k8sclient.getClient();
        Boolean isCompleted = false;
        Job job = (Job) record.value();
        String metaName = job.getMetadata().getName();
        client.batch().jobs().inNamespace("default").createOrReplace(job);
        log.info("K8s创建: ["+metaName+"]");
        while(!isCompleted){
            List<JobCondition> jobCondition = client.batch().jobs().inNamespace("default").list().getItems()
                    .stream().filter(j->j.getMetadata().getName().equals(metaName)).findAny().get().getStatus().getConditions();
            if(!jobCondition.isEmpty()){
                isCompleted = client.batch().jobs().inNamespace("default").list().getItems()
                        .stream().filter(j->j.getMetadata().getName().equals(metaName) && j.getStatus()
                                .getSucceeded().toString().equals("1")).findAny().isPresent();
            }
            Thread.sleep(1000);
            log.info("job: ["+metaName+"] 状态为完成: "+isCompleted);
        }
        log.info("job: ["+metaName+"] 执行完成！");
        acknowledgment.acknowledge();
    }

/*    private KubernetesClient getClient() throws IOException {

        ClassPathResource k8sConfig = new ClassPathResource("kubeconfig/config");
        File confFile = k8sConfig.getFile();
        String kubeConfigContents = new Scanner(confFile).useDelimiter("\\Z").next();
        Config configFile = Config.fromKubeconfig(kubeConfigContents);
        KubernetesClient client = new DefaultKubernetesClient(configFile);
        return client;
    }*/
}
