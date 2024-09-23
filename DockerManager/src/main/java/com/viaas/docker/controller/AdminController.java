package com.viaas.docker.controller;

import com.viaas.docker.common.Result;
import com.viaas.docker.entity.Container;
import com.viaas.docker.entity.dto.AddContainer;
import com.viaas.docker.service.ContainerService;
import com.viaas.docker.service.PacketService;
import com.viaas.docker.service.UserSerivce;
import com.viaas.docker.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
//import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author chen
 * @version 1.0
 * @descript 管理员接口
 * @date 2023/3/2 19:04
 */
@Tag(name = "管理员接口")
@RestController
@RequestMapping("/ibs/api/admin")
//@RequiresRoles("admin")
public class AdminController {
    @Autowired
    private ContainerService containerService;
    @Autowired
    private UserSerivce userSerivce;
    @Autowired
    private PacketService packetService;

    @Autowired
    private JwtUtil jwtUtil;

    /***
     *@descript 操纵容器
     *@param  *
     *@return 更新后的容器信息
     *@author chen  /ibs/api/admin/containers/{id}/{status}
     *@version 1.0
     */
    @Operation(summary = "操纵容器接口", description = "操纵容器接口,status状态有start,stop,pause,restart")
    @GetMapping("/container/{id}/{status}")
    public Result operateContainer(@PathVariable("id") String containerId, @PathVariable("status") String status) {
        return containerService.operateContainer(containerId, status);
    }

    /**
     * 新增容器
     * author chen
     *
     * @param addContainer
     * @return 成功为200 失败为500
     */
    @Operation(summary = "新增容器接口")
    @PostMapping("/containers/create")
    public Result create(@RequestBody AddContainer addContainer, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        long userId = jwtUtil.extractUserId(token);
        containerService.createContainer(addContainer, userId,null);
        return Result.success(200, "创建成功", null);
    }

    /***
     *@descript 查询容器
     *@param  * containerParam 是前端传递过来的参数
     *@return 符合条件的容器集合
     *@author chen /ibs/api/admin/containers/
     *@version 1.0
     */
    @Operation(summary = "查看容器接口")
    @GetMapping("/containers/{page}/{pageSize}")
    public Result<List<Container>> getContainers(@PathVariable("page") Integer page,
                                                 @PathVariable("pageSize") Integer pageSize) {
        return containerService.getContainers( page, pageSize);
    }

    /***
     *@descript 批量生产账号
     *@param count
     *@param token
     *@return bool
     *@author sn
     *@version 1.0
     */
    @Operation(summary = "批量生成账号接口")
    @GetMapping("/batch/{count}/{token}")
    public Boolean batchGenerationUser(@PathVariable("count") int count,
                                       @PathVariable String token) {
        boolean isSuccess = userSerivce.batchGenerationUser(count, token);
        return isSuccess;
    }
}
