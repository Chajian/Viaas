package com.viaas.docker.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.viaas.docker.entity.PermissionGroupPermission;
import com.viaas.docker.mapper.PermissionGroupPermissionMapper;
import com.viaas.docker.service.PermissionGroupPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author sn
 */
@Slf4j
@Service
public class PermissionGroupPermissionSerivceImpl extends ServiceImpl<PermissionGroupPermissionMapper, PermissionGroupPermission> implements PermissionGroupPermissionService {
}
