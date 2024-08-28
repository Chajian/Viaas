package com.viaas.docker.controller;

import com.viaas.docker.common.Constants;
import com.viaas.docker.common.Result;
import com.viaas.docker.entity.Hardware;
import com.viaas.docker.mapper.HardwareMapper;
import com.viaas.docker.service.HardwareService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 硬件接口
 * @author Yanglin
 */
@Tag(name = "硬件信息接口")
@RestController
@RequestMapping("/ibs/api/hardware")
public class HardwardController {

    @Autowired
    private HardwareService hardwareService;

    @Autowired
    private HardwareMapper hardwareMapper;

    /**
     * 获取套餐信息
     * @return
     */
    @Operation(summary = "获取套餐硬件信息接口")
    @GetMapping
    public Result getHardware(@Param("hardwareId") int hardwareId){
        Hardware hardware = hardwareService.getHardwareById(hardwareId);
        if(hardware != null)
            return Result.success(Constants.CODE_200,hardware);
        return Result.error(Constants.HARDWARE_NULL);
    }

    /**
     * 获取硬件信息
     * @return
     */
    @Operation(summary = "获取硬件信息接口")
    @GetMapping("/{page}/{pageSize}")
    public Result getHardwares(@PathVariable("page") int page, @PathVariable("pageSize") int pageSize){
        List<Hardware> hardwares = hardwareService.getHardwares(page,pageSize);
        if(hardwares != null)
            return Result.success(Constants.CODE_200,hardwares);
        return Result.error(Constants.HARDWARE_NULL);
    }
}
