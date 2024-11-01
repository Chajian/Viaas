package com.viaas.certification.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.viaas.certification.entity.UserDTO;
import com.viaas.certification.exception.CertificationCode;
import com.viaas.certification.exception.CertificationException;
import com.viaas.certification.mapper.UserMapper;
import com.viaas.certification.service.UserService;
import com.viaas.idworker.IdWorker;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDTO> implements UserService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private UserMapper userMapper;
    /**
     * 用户的注册
     *
     * @param userDTO
     * @return
     * @author sn
     */
    @Override
    public UserDTO userRegist(UserDTO userDTO) {

        //1.user为空抛出异常
        if (userDTO.getAccount() == null || userDTO.getAccount() == "" || userDTO.getPwd() == null || userDTO.getPwd() == "") {
            throw new CertificationException(CertificationCode.USER_OR_PASSWORD_ERROR);
        }
        //2.用户账号存在抛异常
        UserDTO one = getOne(new LambdaQueryWrapper<UserDTO>().eq(UserDTO::getAccount, userDTO.getAccount()));
        if (one != null) {
            throw new CertificationException(CertificationCode.USER_EXIST);
        }
        //3.注册
        one = new UserDTO();
        save(one);
        return userDTO;
    }

    @Override
    public UserDTO getUserByAccount(@NonNull String account) {
        UserDTO user = userMapper.selectOne(new QueryWrapper<UserDTO>().eq("account",account));
        if (user == null) {
            throw new CertificationException(CertificationCode.USER_OR_PASSWORD_ERROR);
        }
        return user;
    }


    /**
     * @param count 批量生成账号的个数
     * @param token 管理员的token
     * @return
     */
    @Override
    public boolean batchGenerationUser(int count, String token) {

        //判断是否为管理员

        //随机生成账号和密码
        List<UserDTO> userDTOList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            UserDTO userDTO = new UserDTO();
            userDTO.setAccount(String.valueOf(idWorker.nextId()));
            userDTO.setPwd(String.valueOf(idWorker.nextId()));
            userDTOList.add(userDTO);
        }
        //保存
        boolean batch = saveBatch(userDTOList);
        if (!batch) {
            throw new CertificationException("R500", "批量注册失败");
        }
        return batch;

    }
}
