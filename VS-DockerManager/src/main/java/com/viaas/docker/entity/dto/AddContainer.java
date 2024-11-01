package com.viaas.docker.entity.dto;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.viaas.docker.entity.Hardware;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chen
 * @version 1.0
 * @descript 容器的新增信息
 * @date 2023/3/4 20:34
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "新增容器配置")
public class AddContainer {
    @Schema(description = "环境变量XXX=yy，例如MYSQL_ROOT_PASSWORD=123456")
    private List<String> envs;

    @Schema(description = "端口映射HOSTPORT=CONTAINERPORT，例如80:90。把主机80端口映射到容器90端口")
    private List<String> ports;

    @Schema(description = "镜像名称")
    private String imageName;

    @Schema(description = "工作目录")
    private String workingDir;

    @Schema(description = "容器的网络状态，true开启，false关闭")
    private boolean networkDisabled;

    @Schema(description = "容器名称")
    private String containerName;

    @Schema(description = "容器硬件配置")
    private Hardware hardware;

    public List<PortBinding> generatePorts(){
        List<PortBinding> list = new ArrayList<>();
        if (ports != null && !ports.isEmpty()) {
            for (String s : ports) {
                String[] info = s.split(":");
                PortBinding portBinding = new PortBinding(new Ports.Binding("0.0.0.0", info[0]), new ExposedPort(Integer.valueOf(info[1])));
                list.add(portBinding);
            }
        }
        return list;
    }
}
