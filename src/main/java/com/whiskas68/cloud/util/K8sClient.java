package com.whiskas68.cloud.util;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class K8sClient {

    public static KubernetesClient getClient() throws IOException {

        ClassPathResource k8sConfig = new ClassPathResource("kubeconfig/config");
        File confFile = k8sConfig.getFile();
        String kubeConfigContents = new Scanner(confFile).useDelimiter("\\Z").next();
        Config configFile = Config.fromKubeconfig(kubeConfigContents);
        KubernetesClient client = new DefaultKubernetesClient(configFile);
        return client;
    }
}
