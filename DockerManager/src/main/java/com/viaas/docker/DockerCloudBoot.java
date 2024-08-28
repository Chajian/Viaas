package com.viaas.docker;

import com.github.dockerjava.api.DockerClient;
import com.viaas.docker.config.DockerCloudConfig;
import com.viaas.docker.connection.ContainerModel;
import com.viaas.docker.connection.DockerConnection;
import com.viaas.docker.connection.ImageModel;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableScheduling
@MapperScan(basePackages = {"com.viaas.docker.mapper"})
//@EnableAsync
public class DockerCloudBoot {
    public static void main(String[] args) {

        SpringApplication.run(DockerCloudBoot.class, args);
    }
    @Bean
    public DockerClient dockerClient(DockerCloudConfig dockerCloudConfig){
        DockerConnection dockerConnection = new DockerConnection(dockerCloudConfig.getUserName(),dockerCloudConfig.getPassWord(),
                dockerCloudConfig.getEmail(),dockerCloudConfig.getHost(),dockerCloudConfig.getUrl());
        return dockerConnection.connect();
    }

    @Bean
    public ContainerModel containerModel(DockerClient dockerClient) {
        return new ContainerModel(dockerClient);
    }
    @Bean
    public ImageModel imageModel(DockerClient dockerClient){
        return new ImageModel(dockerClient);
    }
}