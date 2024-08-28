package com.viaas.docker.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.viaas.docker.entity.Hardware;
import com.viaas.docker.mapper.HardwareMapper;
import com.viaas.docker.service.HardwareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xieyanglin
 */
@Slf4j
@Service
public class HardwareServiceImpl extends ServiceImpl<HardwareMapper, Hardware> implements HardwareService {

    @Autowired
    HardwareMapper hardwareMapper;

    @Override
    public Hardware getHardwareById(int id) {
        Hardware hardware = hardwareMapper.selectById(id);
        return hardware;
    }

    @Override
    public List<Hardware> getHardwares(int page ,int size) {

        Page<Hardware> p = new Page<>(page, size);
        LambdaQueryWrapper<Hardware> lambdaQueryWrapper = new LambdaQueryWrapper<>();//条件
        Page<Hardware> pageResult = page(p, lambdaQueryWrapper);
        List<Hardware> hardwares = pageResult.getRecords();
        if (pageResult.getSize() <= 0) {
            return null;
        }
        return hardwares;
    }

    @Override
    public Hardware getHardwareByContainerId(String containerId) {



        return null;
    }
}
