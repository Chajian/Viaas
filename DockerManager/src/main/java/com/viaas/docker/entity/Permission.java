package com.viaas.docker.entity;

import lombok.Data;

/**
 * 权限
 * @author Chajian
 */
@Data
public class Permission extends TimeRecord{
    private int id;
    private String name;
    private String description;



}
