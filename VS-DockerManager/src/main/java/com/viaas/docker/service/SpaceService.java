package com.viaas.docker.service;

/**
 *  空间服务
 * @author Yanglin
 */
public interface SpaceService {
    /**
     * 创建用户空间
     * @param userName
     * @return
     */
    boolean createUserSpace(String userName);

    /**
     * 创建用户镜像空间
     * @param account
     * @param image
     * @return
     */
    boolean createImageSpace(String account,String image);


    /**
     * 创建容器空间
     * @param account
     * @param container
     * @return
     */
    boolean createContainerSpace(String account,String container);

    /**
     * 创建用户头像空间
     * 全局公共空间
     * @return
     */
    boolean createUserAvatarSpace();

    /**
     * 获取用户空间
     * @return
     */
    String getUserSpace(String userName);

    /**
     * 获取用户头像地址
     * @return
     */
    String getUserAvatarPath();

    /**
     * 获取用户镜像空间
     * @return
     */
    String getImageSpaceFromUser(String account, String image);

    /**
     * 获取容器空间
     * @param userName
     * @param containerName
     * @return
     */
    String getContainerSpace(String userName,String containerName);


}
