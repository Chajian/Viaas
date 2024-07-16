package com.viaas.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 镜像实体类
 * @author Chajian
 */
@Data
@TableName("image")
@NoArgsConstructor
@AllArgsConstructor
public class Image extends TimeRecord{
    @TableId(value = "id",type = IdType.AUTO)
    private int id;
    private String repository;
    private String tag;
    private String name;
    /*镜像id*/
    private String imageId;
    /*存储大小*/
    private long size;
    /*存储单位*/
    private String unit;
    /*作者*/
    private String author;
}
