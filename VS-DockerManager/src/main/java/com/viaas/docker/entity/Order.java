package com.viaas.docker.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单
 * @author Chajian
 */
@Data
@TableName("orders")
@NoArgsConstructor
@AllArgsConstructor
public class Order extends TimeRecord {
    @TableId(type = IdType.AUTO)
    private int id;
    @TableField("name_o")
    private String name;
    private long userId;
    private float money;
    private String payWay;
    private String containerId;
    private int packetId;
    private String description;
    private String state;//支付状态

}
