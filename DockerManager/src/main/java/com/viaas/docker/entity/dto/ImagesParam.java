package com.viaas.docker.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 镜像的查询条件
 * @version 1.0
 * @descript 镜像的查询条件
 * @date 2023/3/4 21:02
 */
@Data
@Schema(description = "镜像查询条件")
public class ImagesParam {
    @Schema(description = "通过label查询镜像", example = "my-label")
    private String label;

    @Schema(description = "分页配置")
    private PageParam pageParam;

    @Schema(description = "通过id查询", example = "image-123-id")
    private String id;

    @Schema(description = "限制查询数量", example = "10")
    private Integer size;

    @Schema(description = "是否从中央仓库查询", example = "true")
    private boolean center;
}
