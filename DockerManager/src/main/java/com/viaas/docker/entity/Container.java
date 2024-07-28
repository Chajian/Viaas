package com.viaas.docker.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * 容器
 * @author Chajian
 */
@Data
public class Container extends TimeRecord {
    private String id;
    private String imageName;
    @TableField("name_c")
    private String name;
    @TableField("descption")
    private String description;
    @TableField("owner_id")
    private int ownerId;
    private String state;
    @TableField(exist = false)
    private int packetId;
    private Date leaseAt;
    private Date leaseEnd;
}
