package com.whiskas68.cloud.configuration;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "kubernetes")
@Getter
@Setter
@ToString
public class K8sClientConfig {

    private List<Configs> configs;

    @Getter
    @Setter
    @ToString
    static class Configs{
        String name;
        String masterUrl;
        String kubeConfig;
        String token;

        KubernetesClient newClient(){
            System.setProperty(Config.KUBERNETES_KUBECONFIG_FILE,kubeConfig);
            Config config = new ConfigBuilder()
                    .withMasterUrl(this.masterUrl)
                    .withOauthToken(this.token)
                    .build();
            KubernetesClient client = new DefaultKubernetesClient(config);
            return client;
        }

    }
}
