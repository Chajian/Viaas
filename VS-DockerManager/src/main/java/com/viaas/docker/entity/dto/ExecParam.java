package com.viaas.docker.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @descript 容器的执行指令参数
 * @author Yanglin
 * @date 2023/11/08 21:02
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "容器执行指令实体")
public class ExecParam {

    @Schema(description = "执行的指令，例如 cd /home 等", example = "cd /home")
    private String command;

    @Schema(description = "指令执行的路径，例如 /home 表示指令在 /home 路径下执行", example = "/home")
    private String loc;
}
