package com.whiskas68.cloud.controller;



import com.whiskas68.cloud.service.JobEventConsumer;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;


@RestController
@Slf4j
public class PodController {

    @Autowired
    JobEventConsumer jobEventConsumer;

    @RequestMapping("/getPods")
    public String getPods() throws IOException {
        ClassPathResource k8sConfig = new ClassPathResource("kubeconfig/config");
        File confFile = k8sConfig.getFile();
        String kubeConfigContents = new Scanner(confFile).useDelimiter("\\Z").next();
        Config configFile = Config.fromKubeconfig(kubeConfigContents);
        KubernetesClient client = new DefaultKubernetesClient(configFile);
        //KubernetesClient client = new DefaultKubernetesClient("https://10.10.10.131:6443");

        Pod pods = client.pods().inNamespace("default")
                .list()
                .getItems()
                .get(0);
        return pods.toString();
    }

    @RequestMapping(value = "/createPods",method = RequestMethod.POST)
    public void createPods()throws IOException{
        ClassPathResource k8sConfig = new ClassPathResource("kubeconfig/config");
        File confFile = k8sConfig.getFile();
        String kubeConfigContents = new Scanner(confFile).useDelimiter("\\Z").next();
        Config configFile = Config.fromKubeconfig(kubeConfigContents);
        KubernetesClient client = new DefaultKubernetesClient(configFile);
        ClassPathResource podYaml = new ClassPathResource("pods/busybox.yaml");
        InputStream inputYaml = podYaml.getInputStream();
        client.load(inputYaml)
                .inNamespace("default")
                .createOrReplace();
    }


}
