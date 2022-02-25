package com.whiskas68.cloud.configuration;

import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.GenericWebApplicationContext;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
@AllArgsConstructor
public class K8sClientRegister {

    private final K8sClientConfig k8sClientConfig;

    private final GenericWebApplicationContext context;

    @PostConstruct
    public void init(){ initializeBeans(k8sClientConfig.getConfigs());}

    private void initializeBeans(List<K8sClientConfig.Configs> configs){
        configs.forEach(c->context.registerBean(c.name, KubernetesClient.class,c::newClient));
    }
}
