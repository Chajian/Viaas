package com.viaas.docker.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * 套餐
 * @author Chajian
 */
@Data
public class Packet extends TimeRecord {
    private int id;
    private String description;
    @TableField("name_p")
    private String name;
    private int hardwareId;


}
