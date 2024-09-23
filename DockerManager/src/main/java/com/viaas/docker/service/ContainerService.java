package com.viaas.docker.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.dockerjava.api.async.ResultCallback;
import com.viaas.docker.common.Result;
import com.viaas.docker.connection.DashboardResultCallback;
import com.viaas.docker.entity.Container;
import com.viaas.docker.entity.Hardware;
import com.viaas.docker.entity.Order;
import com.viaas.docker.entity.TreeNode;
import com.viaas.docker.entity.dto.AddContainer;
import com.viaas.docker.entity.dto.ContainerParam;

import java.io.InputStream;
import java.text.ParseException;
import java.util.List;

public interface ContainerService extends IService<Container> {
    //查询容器
    Result<List<Container>> getContainers(ContainerParam containerParam, Integer page, Integer pageSize, Integer userId);
    Result<List<Container>> getContainers(Integer page, Integer pageSize, Long userId);

    //管理员接口
    Result<List<Container>> getContainers(Integer page, Integer pageSize);

    //创建容器
    String createContainer(AddContainer addContainer, long userId, Hardware hardware);

    Container getContainersByIdOrStatus(String containerId, String status);

    Container getContainerById(String containerId);

    //操作容器
    Result operateContainer(String containerId, String status);

    //获取容器状态
    String getContainerStatus(String containerId);

    Long createOrder(long userId, Order order) throws ParseException;

    /**
     * 执行指令并返回执行结果
     * @param containerId 容器id
     * @param command 指令
     * @return
     */
    String execCommand(String containerId,String command,String location);

    /**
     * 查询操作容器是否归属User
     * @param id 容器id
     * @param userId
     * @return id不存在返回false||OwnerId！=userId也返回false
     */
    boolean hasContainer(String id,Long userId);

    /**
     * 获取容器的dashboard
     * @param id 容器id
     * @return
     */
    ResultCallback getDashboard(String id);

    /**
     * 关闭dashboard
     * @param dashboardResultCallback
     */
    void closeDashboard(DashboardResultCallback dashboardResultCallback);

    boolean uploadFileToContainer(String containerId, String containerPath, String sourcePath);

    boolean uploadFileToContainer(String containerId, String containerPath, InputStream inputStream);

    byte[] downloadFileFromContainer(String containerId,String target);

    TreeNode getFilesByPath(String containerId, String path);
}
