package com.viaas.docker.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.viaas.docker.entity.Permission;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author sn
 */
public interface PermissionMapper extends BaseMapper<Permission> {

    @Select("select permission.id,permission.name,permission.description from permission left join permission_group_permission \n" +
            "on permission.id = permission_group_permission.permission_id\n" +
            "left join user_permission_group on permission_group_permission.group_id = user_permission_group.group_id\n" +
            "left join users on users.id = user_permission_group.user_id\n" +
            "where users.account = #{account}")
    List<Permission> getPermissionsByUserAccount(@Param("account") String account);

}
