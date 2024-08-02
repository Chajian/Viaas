package com.viaas.docker.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * 硬件资源
 *
 * @author Chajian
 */
@Data
@Schema(description = "容器硬件配置")
public class Hardware extends TimeRecord {
    @Schema(description = "硬件ID", example = "1")
    private int id;

    @NotEmpty(message = "硬件类型不能为空")
    @Schema(description = "CPU类型", example = "Intel i7")
    private String cpuType;

    @Schema(description = "CPU核心数", example = "4")
    @Min(value = 0, message = "CPU核心数不能小于0")
    private int cpuCoreNumber;

    @Schema(description = "网络带宽，单位：MB", example = "1000")
    @Min(value = 0, message = "网络带宽不能小于0")
    private int networkSpeed;

    @Schema(description = "磁盘空间, 单位：GB", example = "500")
    @Min(value = 0, message = "磁盘空间不能小于0")
    private int disk;

    @Schema(description = "内存大小，单位：GB", example = "16")
    @Min(value = 0, message = "内存大小不能小于0")
    private int memory;

    @Schema(description = "硬件配置花费", example = "299.99")
    private float money;
}
