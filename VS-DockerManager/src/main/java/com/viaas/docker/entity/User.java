package com.viaas.docker.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户信息
 *
 * @author Chajian
 */

@TableName("users")
@Data
public class User extends TimeRecord implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String account;
    private String pwd;
    /*头像*/
    private String avatar;
}
