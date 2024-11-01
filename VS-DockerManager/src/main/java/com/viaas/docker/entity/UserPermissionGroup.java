package com.viaas.docker.entity;

import lombok.Data;

/**
 * 用户和权限组的关系
 * @author Chajian
 */
@Data
public class UserPermissionGroup {
    private int id;
    private int groupId;
    private int userId;
}
