package com.viaas.docker.entity;

import lombok.Data;

/**
 * 权限组
 * @author Chajian
 */
@Data
public class PermissionGroup extends TimeRecord{
    private int id;
    private String name;
    private String description;
}
