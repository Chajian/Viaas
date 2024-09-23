package com.viaas.docker.controller;

import com.viaas.docker.common.Constants;
import com.viaas.docker.common.Result;
import com.viaas.docker.entity.dto.UserDto;
import com.viaas.docker.entity.vo.LoginResult;
import com.viaas.docker.service.UserSerivce;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author sn
 * @version 1.0
 * @descript 验证接口
 * 不需要进行身份验证
 * @date 2023/3/2 21:57
 */
@Tag(name = "用户身份验证接口")
@RestController
@RequestMapping("/ibs/api/verify")
public class VerifyController {
    @Autowired
    private UserSerivce userSerivce;

    /**
     * @descript用户的注册
     * @param user
     * @return user
     * @version 1.0
     * @author sn
     */
    @Operation(summary = "注册接口")
    @PostMapping("/register")
    public Result register(@RequestBody UserDto user) {
        return Result.success(Constants.CODE_200, userSerivce.userRegist(user));
    }

    /***
     * @descript 用户的登录
     * @param user *
     * @return  String
     * @author sn
     * @version 1.0
     */
//    @Operation(summary = "登录接口")
//    @PostMapping("/login")
//    public Result userLogin(@RequestBody UserDto user) {
//        String userLoginToken = userSerivce.userLogin(user);
//        LoginResult loginResult = userSerivce.getUserLoginInfo(user.getAccount());
//        loginResult.setToken(userLoginToken);
//        return Result.success(Constants.CODE_200, loginResult);
//    }
}
