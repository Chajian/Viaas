package com.viaas.docker.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.viaas.docker.entity.Hardware;
import com.viaas.docker.entity.Packet;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

public interface PacketMapper extends BaseMapper<Packet> {

    @Insert("REPLACE INTO hardware " +
            "(cpu_type,cpu_core_number,network_speed,memory,disk,money) " +
            "values " +
            "(#{cpuType},#{cpuCoreNumber},#{networkSpeed},#{disk},#{memory},#{money})")
    @Options(useGeneratedKeys=true,keyProperty="id")
    int addGetId(Hardware hardware);
}
