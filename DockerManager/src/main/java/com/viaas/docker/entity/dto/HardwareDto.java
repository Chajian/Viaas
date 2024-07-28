package com.viaas.docker.entity.dto;

import com.viaas.docker.entity.Hardware;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 硬件配置参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "硬件配置参数")
public class HardwareDto extends Hardware {
    @Schema(description = "CPU类型价格", example = "20.0")
    @Min(value = 0, message = "不能小于于0")
    private float cpuTypemoney = 20f;

    @Schema(description = "CPU一个核心的价格", example = "20.0")
    @Min(value = 0, message = "不能小于于0")
    private float cpuCoreNumberMoney = 20f;

    @Schema(description = "1MB网速的价格", example = "20.0")
    @Min(value = 0, message = "不能小于于0")
    private float networkSpeedMoney = 20f;

    @Schema(description = "1GB存储空间的价格", example = "20.0")
    @Min(value = 0, message = "不能小于于0")
    private float diskMoney = 20f;

    @Schema(description = "硬件配置参数的名称", example = "Standard Hardware")
    private String name = "";

    @Schema(description = "硬件配置参数的描述", example = "A standard hardware configuration")
    private String desc = "";
}
