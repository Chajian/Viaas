package com.viaas.entity.vo;

import lombok.Data;

/**
 * dashboard信息
 */
@Data
public class Dashboard {
    String containerName;
    double cpuPortion = -1;
    double memoryPortion = -1;
}
