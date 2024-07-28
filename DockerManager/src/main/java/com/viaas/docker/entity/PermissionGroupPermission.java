package com.viaas.docker.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 权限和权限组的关系
 * @author Chajian
 */
@Data
@TableName("permission_group_permission")
public class PermissionGroupPermission {
    private int id;
    private int groupId;
    private int permissionId;
}
