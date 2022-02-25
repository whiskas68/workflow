FROM openjdk:8u302-jre
MAINTAINER whiskas68
USER root
COPY target/workflow-v1.0.jar /opt/.
COPY src/main/resources/kubeconfig/config /root/.kube/config
EXPOSE 3500
CMD ["java","-jar","/opt/workflow-v1.0.jar"]