package com.whiskas68.cloud;

import com.whiskas68.cloud.util.K8sClient;
import io.fabric8.kubernetes.api.model.batch.Job;
import io.fabric8.kubernetes.api.model.batch.JobBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class PodApiTest {

    @Autowired
    KafkaTemplate kafkaTemplate;

    @Autowired
    K8sClient k8sClient;

    @Test
    public void testPushJobToKafkaSerial(){

        for(Integer id=30;id<31;id++){
            List<String> args = new ArrayList<>();
            args.add(id.toString());
            Job job = createJob("job"+id,"busybox:1.28.4","sleep",args);
            kafkaTemplate.send("job-serial",job);
        }
    }

    @Test
    public void testCreatePod() {
        KubernetesClient client = k8sClient.getClient();
        List<String> args = new ArrayList<>();
        args.add("3");
        Job job = createJob("job1","busybox:1.28.4","sleep",args);
        client.batch().jobs().inNamespace("default").createOrReplace(job);
    }

    private Job createJob(String metaName, String image,String command, List<String> args){
        return new JobBuilder()
                    .withApiVersion("batch/v1")
                    .withNewMetadata()
                    .withName(metaName)
                    .endMetadata()
                    .withNewSpec()
                    .withNewTemplate()
                    .withNewSpec()
                    .addNewContainer()
                    .withName("job")
                    .withImage(image)
                    .withCommand(command)
                    .withImagePullPolicy("IfNotPresent")
                    .withArgs(args)
                    .endContainer()
                    .withRestartPolicy("Never")
                    .endSpec()
                    .endTemplate()
                    .endSpec().build();
    }

}
