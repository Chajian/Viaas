package com.viaas.docker.controller;

import com.viaas.docker.common.Constants;
import com.viaas.docker.common.Result;
import com.viaas.docker.entity.dto.UserParam;
import com.viaas.docker.service.SpaceService;
import com.viaas.docker.service.UserSerivce;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * 用户接口
 */
@Tag(name = "用户接口")
@RestController()
@RequestMapping("/ibs/api/user")
public class UserController {

    @Autowired
    UserSerivce userSerivce;

    @Autowired
    SpaceService spaceService;

    /**
     * update user info
     * @return
     */
    @Operation(summary = "更新用户")
    @PostMapping("/update")
    public Result updateUser(@RequestBody UserParam userParam) {
        //TODO update user info
        return null;
    }

    /**
     * get user info only by own
     * @return
     */
    @Operation(summary = "获取用户信息")
    @GetMapping()
    public Result getUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
//        String userName = JwtUtil.getUserAccount(token);
//        User user = userSerivce.getOne(new QueryWrapper<User>().eq("account", userName));
//        return Result.success(Constants.CODE_200, user);
        return Result.success(Constants.CODE_200, null);
    }

    /**
     * update user avatar
     * @return
     */
    @Operation(summary = "更新用户头像")
    @PostMapping("upavatar")
    public Result updateAvatar(@RequestParam("file") MultipartFile avatar,
                               @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
//        String account = JwtUtil.getUserAccount(token);
        File file = new File(spaceService.getUserAvatarPath());
        if (!file.exists()) {
            spaceService.createUserAvatarSpace();
        }
//        userSerivce.updateAvatar(avatar, account);
        return Result.success(Constants.CODE_200);
    }
}
