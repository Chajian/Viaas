package com.viaas.certification.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.viaas.certification.entity.UserDTO;

/**
 * 用户模块
 *
 * @author sn
 */


public interface UserService extends IService<UserDTO> {
    //用户注册
    UserDTO userRegist(UserDTO userDTO);

    UserDTO getUserByAccount(String account);

    //批量生产账号
    boolean batchGenerationUser(int count, String token);


}
