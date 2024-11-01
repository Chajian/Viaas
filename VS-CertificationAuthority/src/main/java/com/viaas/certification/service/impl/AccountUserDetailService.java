package com.viaas.certification.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.viaas.certification.entity.UserDTO;
import com.viaas.certification.entity.UserDetail;
import com.viaas.certification.mapper.UserMapper;
import com.viaas.certification.service.UserService;
import com.viaas.idworker.IdWorker;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class AccountUserDetailService implements UserDetailsManager {

    @Autowired
    private UserService userService;

    @Autowired
    private IdWorker idWorker;

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        UserDTO userDTO = userService.getUserByAccount(username);
        if (ObjectUtils.isEmpty(userDTO)) {
            throw new UsernameNotFoundException("User not found");
        }
        return new UserDetail(userDTO);
    }


    @Override
    public void createUser(UserDetails user) {
        UserDTO userDTO = UserDTO.buildOf(user);
        userService.userRegist(userDTO);
    }

    @Override
    public void updateUser(UserDetails user) {
        UserDTO userDTO = UserDTO.buildOf(user);
        userService.updateById(userDTO);
    }

    @Override
    public void deleteUser(String username) {
        UserDTO userDTO = userService.getUserByAccount(username);
        if(userDTO!=null){
            userService.removeById(userDTO);
        }
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        //密码校验

        //更新密码
    }

    @Override
    public boolean userExists(String username) {
        UserDTO userDTO = userService.getUserByAccount(username);
        if(userDTO!=null){
            return true;
        }
        return false;
    }
}
