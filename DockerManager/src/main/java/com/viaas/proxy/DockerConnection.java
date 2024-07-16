package com.viaas.proxy;


import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;

import java.time.Duration;

/**
 * docker远程连接器
 */

public class DockerConnection {
    private String userName;
    private String passWord;
    private String host;
    private String email;
    private String url;


    public DockerClientConfig getConfig() {
        return config;
    }

    public void setConfig(DockerClientConfig config) {
        this.config = config;
    }

    public DockerHttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(DockerHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public DockerClient getDockerClient() {
        return dockerClient;
    }

    public void setDockerClient(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    DockerClientConfig config = null;

    DockerHttpClient httpClient = null;

    DockerClient dockerClient = null;
    /**
     *
     * @param userName docker账户名
     * @param passWord 密码
     * @param email 邮箱
     * @param host
     * @param url
     */
    public DockerConnection(String userName,String passWord,String email,String host,String url){
        this.userName = userName;
        this.passWord = passWord;
        this.email = email;
        this.host = host;
        this.url = url;

        config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(host)
                .withDockerTlsVerify(false)
                .withRegistryUrl(url)
                .withRegistryEmail(email)
                .withRegistryUsername(userName)
                .withRegistryPassword(passWord)
                .build();

        httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45))
                .build();
    }

    /**
     * 返回docker实体
     * @return
     */
    public DockerClient connect(){
        return DockerClientImpl.getInstance(config,httpClient);
    }


}


