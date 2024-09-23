package com.viaas.docker.service.serviceImpl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.viaas.docker.common.Constants;
import com.viaas.docker.entity.User;
import com.viaas.docker.entity.Wallet;
import com.viaas.docker.entity.dto.UserDto;
import com.viaas.docker.entity.vo.LoginResult;
import com.viaas.docker.execption.CustomException;
import com.viaas.docker.mapper.UserMapper;
import com.viaas.docker.service.FileService;
import com.viaas.docker.service.SpaceService;
import com.viaas.docker.service.UserSerivce;
import com.viaas.docker.service.WalletService;
import com.viaas.docker.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sn
 */

@Slf4j
@Service
public class UserSerivceImpl extends ServiceImpl<UserMapper, User> implements UserSerivce {

    @Autowired
    private SpaceService spaceService;

    @Autowired
    private FileService  fileService;

    @Autowired
    private UserMapper userMapper;


    @Autowired
    private WalletService walletService;

    /**
     * 用户的注册
     *
     * @param user
     * @return
     * @author sn
     */
    @Override
    public UserDto userRegist(UserDto user) {

        //1.user为空抛出异常
        if (user.getAccount() == null || user.getAccount() == "" || user.getPwd() == null || user.getPwd() == "") {
            throw new CustomException(Constants.CODE_400.getCode(), "添加的用户数据不正确");
        }
        //2.用户账号存在抛异常
        User one = getOne(new LambdaQueryWrapper<User>().eq(User::getAccount, user.getAccount()));
        //2.1判断用户是否为null,是则抛异常
        if (one != null) {
            throw new CustomException(Constants.CODE_400.getCode(), "用户已存在");
        }
        //2.2否则注册
        one = new User();
        BeanUtil.copyProperties(user, one);
        save(one);
        Wallet wallet = new Wallet();
        wallet.setBalance(0);
        wallet.setUserId(one.getId());
        walletService.save(wallet);
        return user;
    }

    /**
     * 用户登录
     *
     * @param user
     * @return
     * @author sn
     */
    @Override
    public String userLogin(UserDto user) {
        //1.判断user是否为空
        if (user == null) {
            throw new CustomException(Constants.CODE_Login_500.getCode(), "用户数据为空");
        }
        //2.判断用户是否存在数据库
        //2.1获取用户数据
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getAccount, user.getAccount());
        User one = getOne(userLambdaQueryWrapper);
        //为null,拦截
        if (one == null) {
            throw new CustomException(Constants.CODE_Login_500.getCode(), "账号不存在");
        }
        if (!one.getPwd().equals(user.getPwd())) {
            throw new CustomException(Constants.CODE_Login_500.getCode(), "密码错误");
        }
        //3.登录成功
        //3.1生成token并返回
//        String token = JwtUtil.sign(one.getAccount(), one.getId());
        String token  = "";
        return token;
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
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setAccount(IdUtil.simpleUUID().substring(0, 7));
            user.setPwd(IdUtil.simpleUUID().substring(0, 7));
            userList.add(user);
        }
        //保存
        boolean batch = saveBatch(userList);
        if (!batch) {
            throw new CustomException(Constants.CODE_BatchREgister_501.getCode(), "批量注册失败");
        }
        return batch;

    }

    @Override
    public boolean updateAvatar(MultipartFile file, String account) {
        if(file.isEmpty()){
            throw new CustomException(Constants.FILE_IS_NULL);
        }
        String userAvatarPath = spaceService.getUserAvatarPath();
        if(!FileUtil.exist(userAvatarPath)){
            spaceService.createUserAvatarSpace();
        }
        try {
            int index = file.getOriginalFilename().lastIndexOf('.');
            String fileName = account+file.getOriginalFilename().substring(index,file.getOriginalFilename().length());
            //step1 do avatar is exist
            User user = userMapper.selectOne(new QueryWrapper<User>().eq("account",account));
            if(FileUtil.exist(userAvatarPath+File.separator+user.getAvatar())){
                //avatar exist, to delete avatar
                FileUtil.del(userAvatarPath+File.separator+user.getAvatar());
            }
            fileService.saveFile(file.getBytes(),fileName,userAvatarPath);
            user.setAvatar(fileName);
            userMapper.updateById(user);
        } catch (IOException e) {
            throw new CustomException(Constants.FILE_WRITE_FAIL);
        }
        return true;
    }

    @Override
    public LoginResult getUserLoginInfo(String account) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("account",account));
        LoginResult loginResult = new LoginResult();
        loginResult.setUserName(user.getAccount());
        loginResult.setAvatar(user.getAvatar());
        Wallet wallet = walletService.getOne(new QueryWrapper<Wallet>().eq("user_id",user.getId()));
        loginResult.setBalance(wallet.getBalance());
        return loginResult;
    }
}
