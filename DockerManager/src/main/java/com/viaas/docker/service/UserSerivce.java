package com.viaas.docker.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.viaas.docker.entity.User;
import com.viaas.docker.entity.dto.UserDto;
import com.viaas.docker.entity.vo.LoginResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户模块
 *
 * @author sn
 */


public interface UserSerivce extends IService<User> {
    //用户注册
    UserDto userRegist(UserDto user);

    //用户登录
    String userLogin(UserDto user);

    //批量生产账号
    boolean batchGenerationUser(int count, String token);

    /*更新用户头像*/
    boolean updateAvatar(MultipartFile file,String account);

    /**
     * 拿取登录之后所需的信息
     * @param account
     * @return
     */
    LoginResult getUserLoginInfo(String account);

}
