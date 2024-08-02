package com.viaas.docker.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.viaas.docker.entity.PermissionGroup;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * PermissionGroupMapper
 * @author Chajian
 */
public interface PermissionGroupMapper extends BaseMapper<PermissionGroup> {

    @Select("select g.name from user_permission_group as u left join permission_group as g on u.group_id = g.id where u.user_id = #{userId}")
    List<String> getGroup(@Param("userId") long userId);

}
