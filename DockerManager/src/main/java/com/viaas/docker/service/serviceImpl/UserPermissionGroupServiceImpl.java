package com.viaas.docker.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.viaas.docker.entity.UserPermissionGroup;
import com.viaas.docker.mapper.UserPermissionGroupMapper;
import com.viaas.docker.service.UserPermissionGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * @author sn
 */
@Slf4j
@Service
public class UserPermissionGroupServiceImpl extends ServiceImpl<UserPermissionGroupMapper, UserPermissionGroup> implements UserPermissionGroupService {
}
