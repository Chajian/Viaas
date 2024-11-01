package com.viaas.docker.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.viaas.docker.entity.Image;
import lombok.Data;

/**
 * Image 视图类
 */
@Data
public class ImageVo {
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
    /*是否最新*/
    private Boolean newest = null;
    /*是否安装*/
    private Boolean install = null;

    public void toImageVo(Image image){
        if (image != null) {
            this.setId(image.getId());
            this.setRepository(image.getRepository());
            this.setTag(image.getTag());
            this.setName(image.getName());
            this.setImageId(image.getImageId());
            this.setSize(image.getSize());
            this.setUnit(image.getUnit());
            this.setAuthor(image.getAuthor());
        }
    }
}
