package com.viaas.docker.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 钱包实体
 * @author Yanglin
 * @date 2023/01/14 11:08
 */
@TableName("wallet")
@Data
public class Wallet extends TimeRecord{
    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    private int userId;
    /*钱包余额*/
    private double balance;
}
