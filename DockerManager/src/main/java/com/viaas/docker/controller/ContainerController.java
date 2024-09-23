package com.viaas.docker.controller;

import cn.hutool.core.io.FileUtil;
import com.viaas.docker.common.Constants;
import com.viaas.docker.common.Result;
import com.viaas.docker.entity.TreeNode;
import com.viaas.docker.entity.dto.AddContainer;
import com.viaas.docker.entity.dto.ContainerParam;
import com.viaas.docker.entity.dto.ExecParam;
import com.viaas.docker.execption.CustomException;
import com.viaas.docker.service.ContainerService;
import com.viaas.docker.service.FileService;
import com.viaas.docker.service.SpaceService;
import com.viaas.docker.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chen
 * @version 1.0
 * @descript 普通用户的容器接口
 * @date 2023/3/4 21:34
 */
@Tag(name = "容器管理接口")
@RestController
@RequestMapping("/ibs/api/containers")
public class ContainerController {
    Map<Integer,String> containerMap = new HashMap<>();

    @Autowired
    private ContainerService containerService;

    @Autowired
    private SpaceService spaceService;

    @Autowired
    private FileService fileService;

    @Autowired
    private JwtUtil jwtUtil;

    /***
     *@descript 获取容器列表
     *@param containerParam page 页数，pagesize页大小，
     * @param token token
     *@return 返回符合条件的容器
     *@author chen
     *@version 1.0
     */
    @Operation(summary = "获取容器列表接口")
    @GetMapping("/get/{page}/{pageSize}")
    public Result getContainers(@RequestBody(required = false) ContainerParam containerParam,
                                @PathVariable(value = "page") Integer page,
                                @PathVariable(value = "pageSize") Integer pageSize,
                                @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        long userId = jwtUtil.extractUserId(token);
        return containerService.getContainers( page, pageSize, userId);
    }

    /***
     *@descript 创建容器
     *@param  *
     *@return  /ibs/api/containers/create
     *@author sn
     *@version 1.0
     */
    @Operation(summary = "创建容器接口")
    @PostMapping("/create")
    public Result createContainer(@RequestBody AddContainer addContainer, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        long userId = jwtUtil.extractUserId(token);
        String containerId = containerService.createContainer(addContainer, userId,null);
        return Result.success(200,"success",containerId);
    }

    /***
     *@descript 操作容器
     *@param  *
     *@retur
     *@author chen  /ibs/api/containers/{id}/{action}
     *@version 1.0
     */
    @Operation(summary = "操作容器接口", description = "操作容器接口,status状态有start,stop,pause,restart")
    @PostMapping("/{id}/{status}")
    public Result operateContainer(@PathVariable("id") String containerId, @PathVariable("status") String status,@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        if(!containerService.hasContainer(containerId, jwtUtil.extractUserId(token))) {
            return Result.error(Constants.CODE_400);
        }

        return containerService.operateContainer(containerId, status);
    }

    /**
     * 执行sh语句
     */
    @Operation(summary = "执行sh指令接口")
    @PostMapping("/{id}/exec")
    public Result execContainer(@PathVariable("id") String containerId,@RequestBody ExecParam exec,@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        if(!containerService.hasContainer(containerId, jwtUtil.extractUserId(token))) {
            return Result.error(Constants.CODE_400);
        }
        String result = containerService.execCommand(containerId,exec.getCommand(), exec.getLoc());
        if(result==null) {
            return Result.error(Constants.EXEC_ERROR);
        }
        return Result.success(Constants.CODE_200,result);
    }

    /**
     * 更新文件到容器
     * @param tagetPath 目标存放地址
     * @return
     */
    @Operation(summary = "上传文件到容器接口")
    @PostMapping("/upload")
    public Result uploadFileToContainer(@RequestParam("file") MultipartFile multipartFile, @RequestHeader(HttpHeaders.AUTHORIZATION) String token,String containerId,String tagetPath){
        String account = jwtUtil.extractUsername(token);
        String savePath = spaceService.getContainerSpace(account,containerId)+tagetPath.replace("/",File.separator);
        try {
            if(!FileUtil.exist(savePath)){
                new File(savePath).mkdirs();
            }
            fileService.saveFile(multipartFile.getInputStream(),multipartFile.getOriginalFilename(),savePath);
        } catch (IOException e) {
            throw new CustomException(Constants.FILE_WRITE_FAIL);
        }
        containerService.uploadFileToContainer(containerId,tagetPath,savePath+File.separator+multipartFile.getOriginalFilename());
        return Result.success(Constants.CODE_200,"文件成功！");
    }

    /**
     * 下载文件从容器
     * @param token
     * @param containerId
     * @param targetPath 目标地址
     * @return
     */
    @Operation(summary = "下载文件到容器接口")
    @PostMapping("/download")
    public Result downloadFileFromContainer(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @Param("containerId") String containerId, @Param("targetPath") String targetPath){

        int lastIndex = targetPath.lastIndexOf('/');
        String fileName = targetPath.substring(lastIndex+1,targetPath.length());
        String account = jwtUtil.extractUsername(token);
        String savePath = spaceService.getContainerSpace(account,containerId)+targetPath.substring(0,lastIndex).replace("/",File.separator);

        byte[] context =  containerService.downloadFileFromContainer(containerId,targetPath);
        try {
            if(!FileUtil.exist(savePath)){
                new File(savePath).mkdirs();
            }
            fileService.saveFile(context,fileName,savePath);
        } catch (FileNotFoundException e) {
            throw new CustomException(Constants.FILE_WRITE_FAIL);
        }
        return Result.success(Constants.CODE_200,"文件成功！");
    }
    @Operation(summary = "获取容器文件树接口")
    @PostMapping("/get/file")
    public Result getContainerFileSystem(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @Param("containerId") String containerId, @Param("targetPath") String targetPath){
        if(!containerService.hasContainer(containerId, jwtUtil.extractUserId(token))) {
            return Result.error(Constants.CODE_400);
        }
        TreeNode treeNode = containerService.getFilesByPath(containerId,targetPath);
        return Result.success(Constants.CODE_200,treeNode);
    }
}
