package com.viaas.docker.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

/**
 * 分页查询参数
 * @author chen
 * @version 1.0
 * @date 2023/3/4 20:15
 */
@Data
@ToString
@Schema(description = "分页配置")
public class PageParam {

    @Schema(description = "当前页码", example = "1")
    private Integer page;

    @Schema(description = "每页数量", example = "10")
    private Integer pageSize;
}
