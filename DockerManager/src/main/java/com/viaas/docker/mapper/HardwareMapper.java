package com.viaas.docker.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.viaas.docker.entity.Hardware;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
public interface HardwareMapper extends BaseMapper<Hardware> {
    @Select("select * from hardware left join packet on packet.hardware_id = hardware.id where packet.id = " +
            "(select packet_id from orders where orders.container_id = #{containerId} and orders.state = '创建成功!')")
    Hardware getHardwareByContainer(@Param("containerId") String containerId);
}
