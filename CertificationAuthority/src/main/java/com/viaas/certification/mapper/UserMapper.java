package com.viaas.certification.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.viaas.certification.entity.UserDTO;
import org.apache.ibatis.annotations.Select;

/**
 * @author sn
 */
public interface UserMapper extends BaseMapper<UserDTO> {

    @Select("select id from users where account =  #{account}")
    public int getUserIdByAccount(String account);


}
