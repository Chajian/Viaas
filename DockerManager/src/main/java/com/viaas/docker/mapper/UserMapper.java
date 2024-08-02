package com.viaas.docker.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.viaas.docker.entity.User;
import org.apache.ibatis.annotations.Select;

/**
 * @author sn
 */
public interface UserMapper extends BaseMapper<User> {

    @Select("select id from users where account =  #{account}")
    public int getUserIdByAccount(String account);


}
