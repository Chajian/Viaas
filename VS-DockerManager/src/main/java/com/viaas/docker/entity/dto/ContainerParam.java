package com.viaas.docker.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @descript 容器的查询条件
 * @author sm
 * @date 2023/3/4 21:02
 * @version 1.0
 */
@Schema(description = "容器查询配置")
@Data
public class ContainerParam {

    @Schema(description = "通过用户名查询容器", example = "user123")
    private String account;

    @Schema(description = "通过id查询容器", example = "container-123-id")
    private String containerId;

    @Schema(description = "通过容器状态查询容器", example = "[\"running\", \"stopped\"]")
    private String[] status;
}
