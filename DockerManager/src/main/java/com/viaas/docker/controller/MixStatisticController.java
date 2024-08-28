package com.viaas.docker.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.viaas.docker.common.Constants;
import com.viaas.docker.common.Result;
import com.viaas.docker.entity.*;
import com.viaas.docker.entity.vo.HardwareVo;
import com.viaas.docker.mapper.*;
import com.viaas.docker.service.ContainerService;
import com.viaas.docker.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

/**
 * 混合数据的controller层
 */
@Tag(name = "混合数据接口")
@RestController
@RequestMapping("/ibs/api/mix")
public class MixStatisticController {

    @Autowired
    HardwareMapper hardwareMapper;
    @Autowired
    ImageMapper imageMapper;
    @Autowired
    ContainerMapper containerMapper;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    ContainerService containerService;

    @Autowired
    PacketMapper packetMapper;

    /**
     * 获取硬件信息通过容器Id
     * @return
     */
    @Operation(summary = "获取硬件信息通过容器ID")
    @GetMapping("/get/{containerId}")
    public Result getHardByContainer(@PathVariable("containerId") String containerId, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        if(!containerService.hasContainer(containerId, JwtUtil.getUserId(token)))
            return Result.error(Constants.CODE_400);

        Container container = containerMapper.selectOne(new QueryWrapper<Container>().eq("id", containerId));
        Order order = null;
        Packet packet = null;
        Hardware hardware = null;

        if(container != null) {
            order = orderMapper.selectOne(new QueryWrapper<Order>().eq("container_id", containerId).eq("state", "支付成功"));
        }

        if(order != null) {
            packet = packetMapper.selectOne(new QueryWrapper<Packet>().eq("id", order.getPacketId()));
        }

        if(packet != null) {
            hardware = hardwareMapper.selectOne(new QueryWrapper<Hardware>().eq("id", packet.getHardwareId()));
        }

        HardwareVo hardwareVo = new HardwareVo();
        if(container != null) {
            hardwareVo.setContainerName(container.getName());
            hardwareVo.setImageName(container.getImageName());
        }

        if(hardware != null) {
            hardwareVo.setMemory(String.valueOf(hardware.getMemory()));
            hardwareVo.setDiskPercent(String.valueOf(hardware.getDisk()));
        }

        hardwareVo.setContainerStatus(containerService.getContainerStatus(containerId));

        return Result.success(Constants.CODE_200, hardwareVo);
    }
}
