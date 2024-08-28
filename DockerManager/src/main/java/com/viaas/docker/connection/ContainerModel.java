package com.viaas.docker.connection;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.LogContainerCmd;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * container服务模块
 * @author Chajian
 */
@Slf4j
public class ContainerModel {
    DockerClient dockerClient = null;


    public ContainerModel(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    /**
     * 查看container信息
     * @param containerId
     */
    public InspectContainerResponse inspectContainer(String containerId){
        InspectContainerResponse containerCmd =  dockerClient.inspectContainerCmd(containerId).exec();
        return containerCmd;
    }

    /**
     * 创建容器
     * @param containerName 容器名
     * @param imageName 镜像id
     * @param ports 端口
     * @param envs 环境
     */
    public synchronized CreateContainerResponse createContainer(String containerName,String imageName, List<PortBinding> ports,List<String> envs){
        HostConfig hostConfig = HostConfig.newHostConfig();
        hostConfig.withPortBindings(ports);
        hostConfig.withNetworkMode("bridge");
        CreateContainerCmd createContainerCmd = dockerClient.createContainerCmd(imageName)
                .withName(containerName);
        if(hostConfig!=null)
            createContainerCmd.withHostConfig(hostConfig);
        if(envs!=null&&envs.size()>0)
            createContainerCmd.withEnv(envs);
        CreateContainerResponse createContainerResponse = createContainerCmd.exec();
        return createContainerResponse;
    }


    /**
     * 创建容器
     * @param cpu 内核数量
     * @param memory 内存-单位m
     * @param disk 硬盘空间-单位G
     * @param network 网络带宽速度
     * @param containerName 容器名
     * @param imageName 镜像名
     * @param ports 端口映射
     */
    public synchronized CreateContainerResponse createContainer(long cpu,long memory,long disk,int network,String containerName,String imageName, List<PortBinding> ports,List<String> env){
        HostConfig hostConfig = HostConfig.newHostConfig();
        hostConfig.withCpuCount(cpu)
                .withMemory(1048576*memory)
                .withDiskQuota(disk*1048576*1024)
                .withNetworkMode("bridge")
                .withPortBindings(ports);
        CreateContainerResponse createContainerResponse = dockerClient.createContainerCmd(imageName)
                .withName(containerName)
                .withHostConfig(hostConfig)
                .withEnv(env)
                .exec();
        return createContainerResponse;
    }

    /**
     * 删除容器
     * @param containerId
     */
    public void deleteContaqqiner(String containerId){
        dockerClient.removeContainerCmd(containerId).exec();
    }

    /**
     * 运行容器
     * @param containerId
     */
    public void startContainer(String containerId){
        dockerClient.startContainerCmd(containerId).exec();
    }

    /**
     * 重启容器
     * @param containerId
     */
    public void restartContainer(String containerId){
        dockerClient.restartContainerCmd(containerId).exec();
    }

    /**
     * 暂停容器
     * @param containerId
     */
    public void pauseContainer(String containerId){
        dockerClient.pauseContainerCmd(containerId).exec();
    }

    /**
     * 停止容器
     * @param containerId
     */
    public void stopContainer(String containerId){
        dockerClient.stopContainerCmd(containerId).exec();
    }

    /**
     * 重命名容器
     * @param newName
     */
    public void renameContainer(String newName){
        dockerClient.renameContainerCmd(newName);
    }


    /**
     * 执行指令
     * @return 返回结果
     */
    public List<String> getLogs(String containerId){
        LogContainerCmd logContainerCmd = dockerClient.logContainerCmd(containerId)
                .withTimestamps(true)
                .withStdOut(true)
                .withStdErr(true);
        List<String> logs = new ArrayList<>();
        try {
            logContainerCmd.exec(new ResultCallback.Adapter<>() {
                @Override
                public void onNext(Frame object) {
                    logs.add(object.toString());
                }
                @Override
                public void onError(Throwable throwable) {
                    logs.add(throwable.getMessage()    );
                }
            }).awaitCompletion();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Size of logs: " + logs.size());
        for (String log: logs) {
            System.out.println("Logging log: " + log);
        }
        return logs;
    }

    /**
     * 执行指令
     * @return 返回结果
     */
    public String execCommand(String containerId,String location, String... command){
        String execId = dockerClient.execCreateCmd(containerId).withCmd(command).withWorkingDir(location).withAttachStdout(true).withAttachStderr(true).exec().getId();
        final StringBuilder output = new StringBuilder();
        try {
            dockerClient.execStartCmd(execId).exec(new ResultCallback.Adapter<>(){
                @Override
                public void onNext(Frame object) {
                    String payLoad = new String(object.getPayload(),StandardCharsets.UTF_8);
                    output.append(payLoad);
                    if(payLoad.contains("failed")||payLoad.contains("No such file or directory")||payLoad.contains("Not a directory"))
                        onError(new Throwable(payLoad));
                    super.onNext(object);
                }

                @Override
                public void onError(Throwable throwable) {
                    super.onError(throwable);
//                    throw new CustomExpection(Constants.EXEC_ERROR.getCode(),throwable.getMessage());
                }

            }).awaitCompletion();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //字符串预处理
        if(output.length()>0&&output.charAt(0)=='['&&output.charAt(output.length()-1)!=']')
            output.deleteCharAt(0);
        return output.toString();
    }


}
