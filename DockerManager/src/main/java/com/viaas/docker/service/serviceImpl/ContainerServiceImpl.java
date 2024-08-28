package com.viaas.docker.service.serviceImpl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.NotFoundException;
import com.viaas.docker.common.Constants;
import com.viaas.docker.common.Result;
import com.viaas.docker.connection.ContainerModel;
import com.viaas.docker.connection.DashboardResultCallback;
import com.viaas.docker.connection.ImageModel;
import com.viaas.docker.entity.*;
import com.viaas.docker.entity.dto.AddContainer;
import com.viaas.docker.entity.dto.ContainerParam;
import com.viaas.docker.execption.CustomException;
import com.viaas.docker.mapper.ContainerMapper;
import com.viaas.docker.mapper.UserMapper;
import com.viaas.docker.service.ContainerService;
import com.viaas.docker.service.OrderService;
import com.viaas.docker.service.PacketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Slf4j
@Service
public class ContainerServiceImpl extends ServiceImpl<ContainerMapper, Container> implements ContainerService {
    private static final long dashboardDelay = 10000L;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ContainerMapper containerMapper;
    @Autowired
    private ImageModel imageModel;
    @Autowired
    @Lazy
    private OrderService orderService;
    @Autowired
    private PacketService packetService;

    @Autowired
    private ContainerModel containerModel;

    @Autowired
    private DockerClient dockerClient;

    @Override
    public Result<List<Container>> getContainers( Integer page, Integer pageSize, Integer userId) {
        //测试用户
        //状态
        Page<Container> p = new Page<>(page, pageSize);
        LambdaQueryWrapper<Container> lambdaQueryWrapper = new LambdaQueryWrapper<>();//条件
        lambdaQueryWrapper.eq(userId != null && userId != -1, Container::getOwnerId, userId); //根据用户找
        Page<Container> pageResult = page(p, lambdaQueryWrapper);
        List<Container> containers = pageResult.getRecords();
        if (pageResult.getSize() <= 0) {
            return null;
        }
        return Result.success(200, "success", containers);
    }

    @Override
    public byte[] downloadFileFromContainer(String containerId, String target) {
        InputStream inputStream = dockerClient.copyArchiveFromContainerCmd(containerId,target)
                .exec();
        byte[] result = null;
        try {
            result = inputStream.readAllBytes();
        } catch (IOException e) {
            throw new CustomException(Constants.FILE_WRITE_FAIL);
        }
        return result;
    }

    @Override
    public TreeNode getFilesByPath(String containerId, String path) {
        TreeNode treeNode = new TreeNode();
        treeNode.setName(path);
        treeNode.setAbsolutePath(path);
        String info = containerModel.execCommand(containerId,path,"ls","-F");
        //pre handler data
        String[] files =  info.split("\n");
        List<String> List = Arrays.asList(files);
        for(String file:List){
            if(StringUtils.isEmpty(file)){
                continue;
            }
            TreeNode treeNode1 = new TreeNode();
            String name = file.substring(0,file.length()-1);
            char type = file.charAt(file.length()-1);
            treeNode1.setName(name);
            treeNode1.setType(String.valueOf(type));
            if(path.equals("/")){
                treeNode1.setAbsolutePath('/'+name);
            }else {
                treeNode1.setAbsolutePath(path + '/' + name);
            }
            treeNode.addNode(treeNode1);
        }
        return treeNode;
    }

    /***
     *@descript 容器列表
     * @param
     *@return
     *@author chen
     *@version 1.0
     */
    @Override
    public Result<List<Container>> getContainers(ContainerParam containerParam, Integer page, Integer pageSize, Integer userId) {
        //测试用户
        //状态
        String[] status = containerParam.getStatus();
        //容器Id
        String containerId = containerParam.getContainerId();
        //用户名
        String account = containerParam.getAccount();
        //主要是管理员可以根据用户名查询容器用的
        if (account != null) {
            //拿到用户Id
            userId = userMapper.getUserIdByAccount(account);
        }
        Page<Container> p = new Page<>(page, pageSize);
        LambdaQueryWrapper<Container> lambdaQueryWrapper = new LambdaQueryWrapper<>();//条件
        lambdaQueryWrapper.eq(userId != null && userId != -1, Container::getOwnerId, userId); //根据用户找
        lambdaQueryWrapper.eq(!StringUtils.isEmpty(containerId), Container::getId, containerId); //根据容器Id查找
        lambdaQueryWrapper.in(status != null, Container::getState, status); //根据状态找
        Page<Container> pageResult = page(p, lambdaQueryWrapper);
        List<Container> containers = pageResult.getRecords();
        if (pageResult.getSize() <= 0) {
            return null;
        }
        return Result.success(200, "success", containers);
    }

    /***
     *@descript 容器列表-管理员
     * @param
     *@return
     *@author Yanglin
     *@version 1.0
     */
    @Override
    public Result<List<Container>> getContainers( Integer page, Integer pageSize) {
        Page<Container> p = new Page<>(page, pageSize);
        LambdaQueryWrapper<Container> lambdaQueryWrapper = new LambdaQueryWrapper<>();//条件
        Page<Container> pageResult = page(p, lambdaQueryWrapper);
        List<Container> containers = pageResult.getRecords();
        if (pageResult.getSize() <= 0) {
            return null;
        }
        return Result.success(200, "success", containers);
    }


    //创建容器
    @Transactional
    @Override
    public synchronized String createContainer(AddContainer addContainer, long userId,Hardware hardware) {
        //用户Id
//        //用户money
//
        long userMoney = userId;
//        //用户选择配置的价格 -->需要根据配置来计算价格
//        //:todo
//        Long userConfigMoney = 200L;
//        //判断余额
//        if (userMoney < userConfigMoney) {
//            throw new CustomExpection(500, "余额不足请充钱");
//        }
//        //要保证原子性 -->可以通过锁来实现 请求量不大的话
//        userMoney = userMoney - userConfigMoney;

        //2.1检查填写的是否为空 envs和imageName
//        check(addContainer);
        //给这个contain name 规范化

        //环境
        List<String> envs = addContainer.getEnvs();
        //镜像名字
        String imageName = addContainer.getImageName();
        //容器资料
        //:todo

        //容器名字
        String containName = userId + "-" + addContainer.getContainerName();

        //2.2 创建容器

        CreateContainerResponse createContainerResponse = containerModel.createContainer(hardware.getCpuCoreNumber(),hardware.getMemory(), hardware.getDisk(),
                hardware.getNetworkSpeed(),containName,imageName,addContainer.generatePorts(),envs);
//        CreateContainerResponse createContainerResponse = containerModel.createContainer(containName, imageName,
//                    addContainer.generatePorts(), envs);


        String containId = null;
        try {
            containId = createContainerResponse.getId();
            //把容器信息同步到数据库
            Container container = new Container();
            container.setOwnerId((int) userId);
            container.setImageName(imageName);
            container.setName(containName);
            container.setCreatedAt(new Date());
            container.setState("1");
            container.setId(containId);
            save(container);
            return containId;
        } catch (Exception e) {
            //创建容器成功了 但是数据库保存出现异常 删除创建容器
            containerModel.deleteContaqqiner(containId);
            throw new CustomException(500, "保持数据异常");
        }
    }

    /**
     * author chen
     * @param addContainer 检查参数是否合法
     */
    private void check(AddContainer addContainer) {
        List<String> env = addContainer.getEnvs();
        String imageName = addContainer.getImageName();
        if(CollectionUtils.isEmpty(env)){
            throw new CustomException(500, "please fill in the envs params");
        }
        if (StringUtils.isEmpty(imageName)) {
            throw new CustomException(500, "please fill in the imageName param");
        }
    }

    //管理员接口
    @Override
    public Container getContainersByIdOrStatus(String containerId, String status) {
        if (StringUtils.isEmpty(containerId)){
            throw new CustomException(500, "容器id不能为空");
        }
        if (StringUtils.isEmpty(status)){
            throw new CustomException(500, "容器状态不能为空");
        }
        LambdaQueryWrapper<Container> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Container::getId, containerId); //根据Id找
        lambdaQueryWrapper.eq(Container::getState, status);  //根据状态找
        Container container = getOne(lambdaQueryWrapper);
        if (container == null) {
            return null;
        }
        return container;
    }

    /**
     *  修改容器状态
     *  author chen
     * @param containerId
     * @param status
     * @return
     */
    @Transactional
    @Override
    public Result operateContainer(String containerId, String status) {
        //根据状态来操作容器
        //1.查找这个容器当前状态
        Container container = getById(containerId);
        if (container == null) {
            throw new CustomException(Constants.CONTAINER_NOT_EXIT);
        }
        if (container.getState().equals(status)) {
            throw new CustomException(Constants.CONTAINER_STATUS_REPEAT);
        }
        try {
            switch (status) {
                case "start":
                    status="1";
                    containerModel.startContainer(containerId);
                    break;
                case "stop":
                    status="2";
                    containerModel.stopContainer(containerId);
                    break;
                case "delete":
                    status="3";
                    containerModel.deleteContaqqiner(containerId);
                    break;
                case "restart":
                    status = "4";
                    containerModel.restartContainer(containerId);
                    break;
                case "pause":
                    status = "5";
                    containerModel.pauseContainer(containerId);
                    break;

            }
        }catch (Exception e){
            throw new CustomException(Constants.CONTAINER_STATUS_FAIL_CHANGE);
        }
        //更新数据库
        container.setState(status);
        container.setUpdatedAt(new Date());
        updateById(container);
        return Result.success(200, "success", "修改成功");
    }

    /**
     * 创建订单
     *
     * @param userId
     * @param order
     * @return
     */
    @Override
    public Long createOrder(long userId, Order order) throws ParseException {

        //创建订单
        if (order == null) {
            throw new CustomException(500, "订单不存在");
        }
        Container containerId = query().eq("id", order.getContainerId()).one();
        Packet packetId = packetService.query().eq("id", order.getPacketId()).one();
        order.setDescription(containerId.getDescription() + packetId.getDescription());
        order.setUserId((int)userId);
        boolean save = orderService.save(order);
        if (!save) {
            throw new CustomException(500, "创建失败");
        }
        //判断订单是否支付成功 1成功 0失败
        if (Integer.parseInt(order.getPayWay()) == 0) {
            //获取当前时间
            String now = DateUtil.now();
            //获取订单创建时间
            Date createdAt = order.getCreatedAt();
            //转换日期格式
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date nowTime = df.parse(now);
            long differTime = nowTime.getTime() - createdAt.getTime();
            int minutes = (int) (differTime / 1000 / 60);

            if (minutes > 15) { //大于15分钟删除表
                removeById(order.getContainerId());
                packetService.removeById(order.getPacketId());
                orderService.removeById(order);
                return Integer.toUnsignedLong(0);
            }
        }

        return Integer.toUnsignedLong(order.getId());
    }

    @Override
    public String execCommand(String containerId,String command,String location){
        String[] commands = command.split(" ");
        String result = containerModel.execCommand(containerId,location,commands);
        return result;
    }


    @Override
    public boolean hasContainer(String id, int userId) {
        LambdaQueryWrapper<Container> lambdaQueryWrapper = new LambdaQueryWrapper<>();//条件
        lambdaQueryWrapper.eq(Container::getOwnerId,userId);
        lambdaQueryWrapper.eq(Container::getId,id);
        Container container = containerMapper.selectOne(lambdaQueryWrapper);
        return container!=null;
    }

    @Override
    public ResultCallback getDashboard(String id) {
            DashboardResultCallback resultCallback = new DashboardResultCallback();
            dockerClient.statsCmd(id)
                    .exec(resultCallback);
//            ResultCallback resultCallback = dockerClient.statsCmd(id)
//                .exec(new ResultCallback.Adapter<>(){
//                    @Override
//                    public void onNext(Statistics object) {
//                        super.onNext(object);
//                        System.out.println("进程信息");
//                        Dashboard dashboard = new Dashboard();
//
//                        if(object.getCpuStats().getSystemCpuUsage()!=null&&object.getPreCpuStats().getSystemCpuUsage()!=null) {
//                            double cpuDelta = object.getCpuStats().getCpuUsage().getTotalUsage() - object.getPreCpuStats().getCpuUsage().getTotalUsage();
//                            double cpuSysDetal = object.getCpuStats().getSystemCpuUsage() - object.getPreCpuStats().getSystemCpuUsage();
//                            double cpuPortion = cpuDelta / cpuSysDetal * object.getCpuStats().getOnlineCpus() * 100f;
//                            dashboard.setCpuPortion((float) cpuPortion);
//                        }
//                        if(object.getMemoryStats().getUsage()!=null){
////                    double usedMemory = object.getMemoryStats().getUsage()-object.getMemoryStats().getStats().getCache();
//                            double usedMemory = object.getMemoryStats().getUsage();
//                            double availableMemory = object.getMemoryStats().getLimit();
//                            double memoryPortion = usedMemory/availableMemory*100f;
//                            dashboard.setMemoryPortion(memoryPortion);
//                        }
//                        dashboard.setContainerName(id);
//                        temp[0] = dashboard;
//                    }
//                }).awaitCompletion();
            return resultCallback;

    }

    @Override
    @Async
    public void closeDashboard(DashboardResultCallback dashboardResultCallback) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new CustomException(Constants.CLOSE_DASH_BOARD);
        }
        dashboardResultCallback.onComplete();
    }


    @Override
    public Container getContainerById(String containerId) {
        LambdaQueryWrapper<Container> lambdaQueryWrapper = new LambdaQueryWrapper<>();//条件
        lambdaQueryWrapper.eq(Container::getId,containerId);
        Container container = containerMapper.selectOne(lambdaQueryWrapper);

        return container;
    }


    @Override
    public String getContainerStatus(String containerId) {
        try {
            InspectContainerResponse response = dockerClient.inspectContainerCmd(containerId).exec();
            return response.getState().getStatus();
        }
        catch (NotFoundException e){
            return "exited";
        }

    }

    @Override
    public boolean uploadFileToContainer(String containerId, String containerPath, String sourcePath) {
        dockerClient.copyArchiveToContainerCmd(containerId)
                .withHostResource(sourcePath)
                .withRemotePath(containerPath)
                .exec();
        return true;
    }

    @Override
    public boolean uploadFileToContainer(String containerId, String containerPath, InputStream inputStream) {
        dockerClient.copyArchiveToContainerCmd(containerId)
                .withTarInputStream(inputStream)
                .withRemotePath(containerPath)
                .exec();
        return true;
    }
}
