package com.viaas.docker.entity.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.viaas.docker.entity.Hardware;
import com.viaas.docker.entity.Packet;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 套餐信息
 */
@Schema(description = "套餐参数")
@Data
public class PacketVo {

    @Schema(description = "套餐ID", example = "1")
    private int id;

    @Schema(description = "套餐描述", example = "This is a standard packet.")
    private String description;

    @Schema(description = "套餐名称", example = "Standard Packet")
    @TableField("name_p")
    private String name;

    @Schema(description = "套餐硬件参数ID", example = "101")
    private int hardwareId;

    @Schema(description = "套餐硬件信息")
    private Hardware hardware;

    public void toPacketVo(Packet packet){
        this.id = packet.getId();
        this.description = packet.getDescription();
        this.name = packet.getName();
        this.hardwareId = packet.getHardwareId();
    }

    public Packet toPacket(){
        Packet packet = new Packet();
        packet.setName(this.name);
        packet.setId(this.id);
        packet.setHardwareId(this.hardwareId);
        packet.setDescription(this.description);
        return packet;
    }
}
