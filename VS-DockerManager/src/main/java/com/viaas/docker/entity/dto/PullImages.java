package com.viaas.docker.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 拉取镜像参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "拉取镜像参数")
public class PullImages {

    @Schema(description = "通过镜像名查询", example = "ubuntu")
    private String name;

    @Schema(description = "通过tag查询", example = "latest")
    private String tag;

    @Schema(description = "通过镜像id查询", example = "123")
    private int imageId;

    @Schema(description = "通过状态查询", example = "active")
    private String status;
}
