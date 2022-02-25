package com.whiskas68.cloud.util;

import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.GenericWebApplicationContext;


@AllArgsConstructor
@Component
public class K8sClient {

    private final GenericWebApplicationContext context;

    public KubernetesClient getClient(){

        KubernetesClient client = (KubernetesClient) context.getBean("workflow");
        return client;

    }
}
